package com.guru.takeaway.di

import com.guru.takeaway.ui.utils.SchedulersProvider
import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.domain.RestaurantDataSource
import com.guru.takeaway.ui.utils.ISchedulersProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ManagersModule {
    @Binds
    abstract fun bindsDataSource(dataSource: RestaurantDataSource): IRestaurantDataSource

    @Binds
    abstract fun bindsSchedulers(schedulersProvider: SchedulersProvider): ISchedulersProvider
}