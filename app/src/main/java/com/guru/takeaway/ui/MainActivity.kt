package com.guru.takeaway.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guru.takeaway.R
import com.guru.takeaway.ui.adapter.RestaurantsAdapter
import com.guru.takeaway.ui.utils.loadingstate.LoadingState
import com.guru.takeaway.ui.viewmodel.MainViewModel
import com.guru.takeaway.ui.viewmodel.MainViewModelFactory
import com.jakewharton.rxbinding2.widget.textChanges
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_bar_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var firstTime: Boolean = true
    private var disposable = Disposables.disposed()

    @Inject
    lateinit var factory: MainViewModelFactory

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    private val mAdapter: RestaurantsAdapter by lazy {
        RestaurantsAdapter(mutableListOf())
    }

    private val itemDecoration by lazy {
        val margin: Int = resources.getDimension(R.dimen.margin_4).toInt()
        val lateralMargin: Int = resources.getDimension(R.dimen.margin_8).toInt()

        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
        mainRv.adapter = mAdapter

        initSearchInputListener()

        viewModel.getRestaurants().observe(this, Observer {
            mAdapter.updateList(it)

            if (firstTime) {
                viewModel.subscribeForSearchChanges()
                firstTime = false
            }
        })

        cancelSearch.setOnClickListener {
            searchInput.text.clear()
            mAdapter.updateList(viewModel.allItems)
        }

        viewModel.subscribeToError().observe(this, Observer {
            if (LoadingState.ERROR_STATE.error == it.error) {
                mainRv.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun initSearchInputListener() {
        searchInput.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.publishSearchChanges(searchInput.text.toString())
                dismissKeyboard(view.windowToken)
                true
            } else {
                false
            }
        }

        if (disposable.isDisposed) {
            disposable = searchInput
                .textChanges()
                .debounce(3, TimeUnit.SECONDS)
                .filter { s -> s.isNotEmpty() }
                .subscribe {
                    viewModel.publishSearchChanges(searchInput.text.toString())
                    dismissKeyboard(searchInput.windowToken)
                }
        }
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        viewModel.clear()
    }
}