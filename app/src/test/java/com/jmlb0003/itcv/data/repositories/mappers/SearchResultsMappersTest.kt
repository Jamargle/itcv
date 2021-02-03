package com.jmlb0003.itcv.data.repositories.mappers

import org.junit.Assert.assertEquals
import org.junit.Test
import com.jmlb0003.itcv.data.network.user.response.search.ResultItem as ApiSearchResult

class SearchResultsMappersTest {

    @Test
    fun `mapToDomain converts a search result response to a domain model SearchResult`() {
        val expectedUsername = "some name"
        val expectedAvatarUrl = "some website"
        val expectedGithubUrl = "some github url"
        val searchResponse = getFakeApiSearchResult().copy(
            username = expectedUsername,
            avatarUrl = expectedAvatarUrl,
            githubUrl = expectedGithubUrl
        )

        val result = SearchResultsMappers.mapToDomain(searchResponse)

        with(result) {
            assertEquals(expectedUsername, title)
            assertEquals(expectedAvatarUrl, imageUrl)
        }
    }

    private fun getFakeApiSearchResult() =
        ApiSearchResult(
            username = "uuu",
            avatarUrl = "aaa",
            githubUrl = "ggg"
        )
}
