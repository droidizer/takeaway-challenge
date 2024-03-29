package com.guru.takeaway.ui.di

import com.guru.takeaway.ui.utils.ISchedulersProvider
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.ui.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun providesFactory(dataSource: IRestaurantDataSource, schedulersProvider: ISchedulersProvider) =
        MainViewModelFactory(dataSource, schedulersProvider)
}
