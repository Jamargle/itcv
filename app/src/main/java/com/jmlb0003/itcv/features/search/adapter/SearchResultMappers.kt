package com.jmlb0003.itcv.features.search.adapter

import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.features.search.adapter.SearchResult as ResultListItem

object SearchResultMappers {

    fun SearchResult.toSearchResultListItem() =
        ResultListItem(
            username = title,
            isFavorite = false
        )
}
