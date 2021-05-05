package com.jmlb0003.itcv.features.search.adapter

import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.features.search.adapter.SearchResult as ResultListItem

object SearchResultMappers {

    fun mapToSearchResultListItem(results: List<SearchResult>) =
        with(results) {
            map {
                ResultListItem(
                    username = it.title,
                    isFavorite = false
                )
            }
        }
}
