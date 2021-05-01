package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.data.network.user.response.search.ResultItem as ApiSearchResult

object SearchResultsMappers {

    fun mapToDomain(result: ApiSearchResult) =
        with(result) {
            SearchResult(
                title = username,
                imageUrl = avatarUrl
            )
        }
}
