package com.guru.takeaway.ui.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.model.Restaurant
import io.reactivex.Completable
import io.reactivex.disposables.Disposables
import java.util.*

class MainViewModel constructor(private val dataSource: IRestaurantDataSource) : ViewModel() {

    private var disposable = Disposables.disposed()
    private var originalItemsLiveData = MutableLiveData<List<Restaurant>>()

    val allItems: MutableList<Restaurant> = mutableListOf()
    val filteredItems: MutableList<Restaurant> = mutableListOf()
    val oldFilteredItems: MutableList<Restaurant> = mutableListOf()

    fun search(query: String): Completable = Completable.create {
        if (!TextUtils.isEmpty(query)) {
            val filter = allItems.filter {
                it.name.toLowerCase(Locale.GERMAN).contains(query.toLowerCase(Locale.GERMAN))
            }.toList()

            filteredItems.clear()
            filteredItems.addAll(filter)
            it.onComplete()
        }
    }

    fun getRestaurants(): LiveData<List<Restaurant>> {
        disposable = dataSource.getRestaurantList()
            .subscribe {
                allItems.addAll(it)
                originalItemsLiveData.postValue(allItems)
            }
        return originalItemsLiveData
    }

    fun clear() {
        dataSource.clear()
    }
}