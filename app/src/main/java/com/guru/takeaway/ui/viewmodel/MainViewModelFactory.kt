package com.guru.takeaway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guru.takeaway.domain.IRestaurantDataSource
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val dataSource: IRestaurantDataSource) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(
        dataSource
    ) as T
}
