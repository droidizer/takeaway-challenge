package com.guru.takeaway.domain

import com.guru.takeaway.model.Restaurant
import io.reactivex.Observable

interface IRestaurantDataSource {
    fun getRestaurantList(): Observable<List<Restaurant>>
    fun clear()
}
