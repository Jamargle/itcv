package com.jmlb0003.itcv.features.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jmlb0003.itcv.R

class SearchResultsAdapter(
    private val onResultClicked: (SearchResult) -> Unit
) : ListAdapter<SearchResult, SearchResultViewHolder>(SearchResultDiffCallback) {

    fun setSearchResults(results: List<SearchResult>) {
        submitList(results)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchResultViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_search, parent, false)
    )

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bindView(getItem(position), onResultClicked)
    }
}
