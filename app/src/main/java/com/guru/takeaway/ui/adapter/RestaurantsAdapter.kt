package com.guru.takeaway.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.guru.takeaway.R
import com.guru.takeaway.model.Restaurant


class RestaurantsAdapter constructor(private var items: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurants_item, parent, false)
        return RestaurantsViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        holder.bindTo(items.get(position))
    }

    fun updateList(newItems: List<Restaurant>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun clear() {
        items = emptyList()
        notifyDataSetChanged()
    }
}
