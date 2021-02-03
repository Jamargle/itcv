package com.jmlb0003.itcv.features.search

import com.jmlb0003.itcv.features.search.adapter.SearchResult

sealed class SearchViewStateList {
    object Loading : SearchViewStateList()
    object Empty : SearchViewStateList()
    object Error : SearchViewStateList()
    class ListOfResults(val results: List<SearchResult>) : SearchViewStateList()
}
