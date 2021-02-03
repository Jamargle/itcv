package com.jmlb0003.itcv.features.search

import androidx.lifecycle.MutableLiveData
import com.jmlb0003.itcv.features.search.adapter.SearchResult

class SearchViewState {

    val viewState: MutableLiveData<SearchViewStateList> = MutableLiveData()

    fun displayLoading() = viewState.postValue(SearchViewStateList.Loading)

    fun displayResults(results: List<SearchResult>) = viewState.postValue(SearchViewStateList.ListOfResults(results))

    fun displayEmptyScreen() = viewState.postValue(SearchViewStateList.Empty)

    fun displayErrorScreen() = viewState.postValue(SearchViewStateList.Error)
}
