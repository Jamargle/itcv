package com.jmlb0003.itcv.features.search.adapter

import androidx.recyclerview.widget.DiffUtil

object SearchResultDiffCallback : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem.username == newItem.username

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem.username == newItem.username &&
                oldItem.isFavorite == newItem.isFavorite
}
