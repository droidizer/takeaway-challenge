package com.guru.takeaway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guru.takeaway.ui.utils.ISchedulersProvider
import com.guru.takeaway.domain.IRestaurantDataSource
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory @Inject constructor(private val dataSource: IRestaurantDataSource, private val schedulersProvider: ISchedulersProvider) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(dataSource, schedulersProvider) as T
}
