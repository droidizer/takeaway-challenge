package com.guru.takeaway.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guru.takeaway.ui.utils.ISchedulersProvider
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.model.Restaurant
import com.guru.takeaway.ui.utils.loadingstate.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class MainViewModel constructor(
    private val dataSource: IRestaurantDataSource,
    private val schedulerProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable = Disposables.disposed()
    private var itemsLiveData = MutableLiveData<MutableList<Restaurant>>()
    private var loadingStateLiveData = MutableLiveData<LoadingState>()

    private var searchNotifierDisposable = Disposables.disposed()
    private val searchPublishSubject: PublishSubject<String> = PublishSubject.create()

    private val originalItems: MutableList<Restaurant> = mutableListOf()
    val allItems: MutableList<Restaurant> = mutableListOf()

    fun getRestaurants(): LiveData<MutableList<Restaurant>> {
        if (disposable.isDisposed) {
            disposable = dataSource.getRestaurantList()
                .compose(schedulerProvider.applySchedulers())
                .subscribe({
                    allItems.addAll(it)
                    LoadingState.SUCCESS_STATE.data = it
                    itemsLiveData.postValue(LoadingState.SUCCESS_STATE.data)
                }, {
                    LoadingState.ERROR_STATE.error = it
                    loadingStateLiveData.postValue(LoadingState.ERROR_STATE)
                })
        }

        return itemsLiveData
    }

    fun subscribeForSearchChanges() {
        if (searchNotifierDisposable.isDisposed) {
            searchNotifierDisposable =
                searchPublishSubject
                    .filter { s -> s.isNotEmpty() }
                    .map { search ->
                        originalItems.clear()
                        originalItems.addAll(allItems)

                        search.replace("\\s".toRegex(), "")
                        return@map originalItems.filter {
                            it.name.toLowerCase(Locale.GERMAN).contains(search.toLowerCase(Locale.GERMAN))
                        }.toList()
                    }

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::search, this::handleError)
        }
    }

    fun publishSearchChanges(text: String) {
        searchPublishSubject.onNext(text)
    }

    fun subscribeToError(): LiveData<LoadingState> {
        return loadingStateLiveData
    }

    private fun search(filter: List<Restaurant>) {
        LoadingState.SUCCESS_STATE.data = filter
        itemsLiveData.postValue(LoadingState.SUCCESS_STATE.data)
    }

    private fun handleError(it: Throwable?) {
        LoadingState.ERROR_STATE.error = it
        loadingStateLiveData.postValue(LoadingState.ERROR_STATE)
    }

    fun clear() {
        dataSource.clear()
    }
}