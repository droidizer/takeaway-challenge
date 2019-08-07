package com.guru.takeaway.domain

import android.content.res.Resources
import com.google.gson.Gson
import com.guru.takeaway.R
import com.guru.takeaway.model.Restaurant
import com.guru.takeaway.model.RestaurantModel
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class RestaurantDataSource
@Inject constructor(private val resources: Resources, private val gson: Gson) : IRestaurantDataSource {

    private var restaurantDisposable = Disposables.disposed()

    override fun getRestaurantList(): Single<List<Restaurant>> {

        return Single.fromCallable {
            val jsonString = resources.openRawResource(R.raw.restaurants)
                .bufferedReader().use { it.readText() }

            val restaurantModel = gson.fromJson(jsonString, RestaurantModel::class.java)

            val open = mutableListOf<Restaurant>()
            val others = mutableListOf<Restaurant>()

            restaurantModel.restaurants.forEach {
                if (it.status == "open") open.add(it) else others.add(it)
            }

            open.addAll(others)
            open
        }
    }

    override fun clear() {
        restaurantDisposable.dispose()
    }
} 