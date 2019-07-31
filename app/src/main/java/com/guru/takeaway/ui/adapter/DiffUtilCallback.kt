package com.guru.takeaway.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.guru.takeaway.model.Restaurant

class DiffUtilCallback(private val oldList: List<Restaurant>, private val newList: List<Restaurant>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}