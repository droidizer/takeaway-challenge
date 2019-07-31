package com.guru.takeaway.ui

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guru.takeaway.R
import com.guru.takeaway.ui.adapter.RestaurantsAdapter
import com.guru.takeaway.ui.viewmodel.MainViewModel
import com.guru.takeaway.ui.viewmodel.MainViewModelFactory
import com.jakewharton.rxbinding2.widget.textChanges
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: RestaurantsAdapter
    private val disposable = CompositeDisposable()

    @Inject
    lateinit var factory: MainViewModelFactory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    private val itemDecoration by lazy {
        val margin: Int = resources.getDimension(R.dimen.margin_4).toInt()
        val lateralMargin: Int = resources.getDimension(R.dimen.margin_8).toInt()

        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State){
                outRect.bottom = margin
                outRect.top = margin
                outRect.left = lateralMargin
                outRect.right = lateralMargin
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRv.layoutManager = LinearLayoutManager(this)
        mainRv.addItemDecoration(itemDecoration)
        mainRv.adapter = RestaurantsAdapter(listOf())

        cancelSearch.setOnClickListener {
            searchInput.text.clear()
            mAdapter.clear()
            mAdapter.updateList(viewModel.allItems)
        }

        viewModel.getRestaurants().observe(this, Observer {
            mainRv.adapter = RestaurantsAdapter(it)
            // Required for Step 2
            mAdapter = mainRv.adapter as RestaurantsAdapter
            subscribeToSearchChanges()
        })
    }

    private fun subscribeToSearchChanges() {
        searchInput
            .textChanges()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .filter { res -> !TextUtils.isEmpty(res.toString()) }
            .subscribe {
                viewModel
                    .search(it.toString())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        viewModel.oldFilteredItems.clear()
                        viewModel.oldFilteredItems.addAll(viewModel.filteredItems)
                        // Step 2
                        mAdapter.updateList(viewModel.filteredItems)
                    }.addTo(disposable)
            }.addTo(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        viewModel.clear()
    }
}