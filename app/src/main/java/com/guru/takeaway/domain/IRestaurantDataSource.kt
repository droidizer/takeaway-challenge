package com.guru.takeaway.domain

import com.guru.takeaway.model.Restaurant
import io.reactivex.Single

interface IRestaurantDataSource {
    fun getRestaurantList(): Single<List<Restaurant>>
    fun clear()
}
