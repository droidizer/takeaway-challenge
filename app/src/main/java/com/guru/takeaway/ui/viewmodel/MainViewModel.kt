package com.guru.takeaway.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.model.Restaurant
import com.guru.takeaway.ui.utils.ISchedulersProvider
import com.guru.takeaway.ui.utils.loadingstate.LoadingState
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel constructor(
    private val dataSource: IRestaurantDataSource,
    private val schedulerProvider: ISchedulersProvider
) : ViewModel() {

    private var itemsDisposable = Disposables.disposed()
    private var searchNotifierDisposable = Disposables.disposed()
    private val searchPublishSubject: PublishSubject<String> = PublishSubject.create()
    private val originalItems: MutableList<Restaurant> = mutableListOf()

    var itemsLiveData = MutableLiveData<MutableList<Restaurant>>()
    var loadingStateLiveData = MutableLiveData<LoadingState>()
    val allItems: MutableList<Restaurant> = mutableListOf()

    fun loadRestaurants() {
        if (itemsDisposable.isDisposed) {
            itemsDisposable = dataSource.getRestaurantList()
                .subscribeOn(schedulerProvider.getIOScheduler())
                .observeOn(schedulerProvider.getUIScheduler())
                .subscribe({
                    allItems.addAll(it)
                    LoadingState.SUCCESS_STATE.data = it
                    itemsLiveData.postValue(LoadingState.SUCCESS_STATE.data)
                }, {
                    LoadingState.ERROR_STATE.error = it
                    loadingStateLiveData.postValue(LoadingState.ERROR_STATE)
                })
        }
    }

    fun subscribeToRestaurants(): LiveData<MutableList<Restaurant>> {
        return itemsLiveData
    }

    fun subscribeForSearchChanges() {
        if (searchNotifierDisposable.isDisposed) {
            searchNotifierDisposable =
                searchPublishSubject
                    .debounce(1, TimeUnit.SECONDS, schedulerProvider.getComputationScheduler())
                    .filter { s -> s.isNotEmpty() }
                    .map { search ->
                        originalItems.clear()
                        originalItems.addAll(allItems)

                        return@map originalItems.filter {
                            trimWhiteSpaces(it.name).toLowerCase(Locale.GERMAN)
                                .contains(trimWhiteSpaces(search).toLowerCase(Locale.GERMAN))
                        }.toList()
                    }

                    .observeOn(schedulerProvider.getUIScheduler())
                    .subscribe(this::filteredItems, this::handleError)
        }
    }

    private fun trimWhiteSpaces(untrimmed: String) = untrimmed.replace("\\s".toRegex(), "")

    fun publishSearchChanges(text: String) {
        searchPublishSubject.onNext(text)
    }

    fun subscribeToError(): LiveData<LoadingState> {
        return loadingStateLiveData
    }

    private fun filteredItems(filter: List<Restaurant>) {
        LoadingState.SUCCESS_STATE.data = filter
        itemsLiveData.postValue(LoadingState.SUCCESS_STATE.data)
    }

    private fun handleError(it: Throwable?) {
        LoadingState.ERROR_STATE.error = it
        loadingStateLiveData.postValue(LoadingState.ERROR_STATE)
    }

    fun clear() {
        dataSource.clear()
        itemsDisposable.dispose()
        searchNotifierDisposable.dispose()
    }
}