package com.jmlb0003.itcv.features.search.adapter

import com.jmlb0003.itcv.domain.model.SearchResult
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchResultMappersTest {

    private val mapper = SearchResultMappers

    @Test
    fun `on mapToSearchResultListItem converts given domain results to list of presentation results`() {
        val title1 = "Title 1"
        val image1 = "Image 1"
        val result1 = SearchResult(
            title = title1,
            imageUrl = image1
        )
        val title2 = "Title 2"
        val image2 = "Image 2"
        val result2 = SearchResult(
            title = title2,
            imageUrl = image2
        )

        val presentationResults = mapper.mapToSearchResultListItem(listOf(result1, result2))

        with(presentationResults[0]) {
            assertEquals(title1, username)
            assertEquals(false, isFavorite)
        }
        with(presentationResults[1]) {
            assertEquals(title2, username)
            assertEquals(false, isFavorite)
        }
    }
}
