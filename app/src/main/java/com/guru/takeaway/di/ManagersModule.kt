package com.guru.takeaway.di

import com.guru.takeaway.domain.IRestaurantDataSource
import com.guru.takeaway.domain.RestaurantDataSource
import dagger.Binds
import dagger.Module

@Module
abstract class ManagersModule {

    @Binds
    abstract fun bindsDataSource(apiManager: RestaurantDataSource): IRestaurantDataSource
}