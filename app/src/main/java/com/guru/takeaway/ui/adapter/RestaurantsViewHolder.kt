package com.guru.takeaway.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guru.takeaway.R
import com.guru.takeaway.model.Restaurant

class RestaurantsViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.title)
    private val subtitle = view.findViewById<TextView>(R.id.subtitle)
    private val minimumTime = view.findViewById<TextView>(R.id.minimumTime)
    private val averagePrice = view.findViewById<TextView>(R.id.averagePrice)
    private val deliveryCost = view.findViewById<TextView>(R.id.deliveryCost)

    fun bindTo(restaurant: Restaurant?) {
        restaurant ?: return
        title.text = restaurant.name
        subtitle.text = restaurant.sortingValues.ratingAverage.toString()
        minimumTime.text =
            if (restaurant.status == "open") restaurant.sortingValues.minCost.toString() else restaurant.status
        averagePrice.text = restaurant.sortingValues.averageProductPrice.toString()
        val deliveryCosts = restaurant.sortingValues.deliveryCosts
        deliveryCost.text = if (deliveryCosts == 0) context.getString(R.string.free) else deliveryCosts.toString()
    }
}
