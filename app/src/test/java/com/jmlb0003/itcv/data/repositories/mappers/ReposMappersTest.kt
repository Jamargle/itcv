package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class ReposMappersTest {

    @Test
    fun `mapToDomain converts a repo response to a domain model repo`() {
        val expectedName = "some name"
        val expectedDescription = "some description"
        val expectedWebsiteUrl = "some website"
        val expectedRepoUrl = "some github url"
        val repoResponse = getFakeRepoResponse().copy(
            name = expectedName,
            description = expectedDescription,
            websiteUrl = expectedWebsiteUrl,
            repoUrl = expectedRepoUrl
        )

        val result = ReposMappers.mapToDomain(repoResponse)

        with(result) {
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedWebsiteUrl, website)
            assertEquals(expectedRepoUrl, repoUrl)
        }
    }

    @Test
    fun `when right repo response does not contain description it converts it to empty string`() {
        val repoResponse = getFakeRepoResponse().copy(description = null)
        val result = ReposMappers.mapToDomain(repoResponse)
        assertEquals("", result.description)
    }

    @Test
    fun `when right repo response does not contain websiteUrl it converts it to empty string`() {
        val repoResponse = getFakeRepoResponse().copy(websiteUrl = null)
        val result = ReposMappers.mapToDomain(repoResponse)
        assertEquals("", result.website)
    }

    @Test
    fun `when right repo response does not contain repoUrl it converts it to empty string`() {
        val repoResponse = getFakeRepoResponse().copy(repoUrl = null)
        val result = ReposMappers.mapToDomain(repoResponse)
        assertEquals("", result.repoUrl)
    }

    private fun getFakeRepoResponse() =
        RepoResponse(
            name = "zzz",
            description = "ddd",
            websiteUrl = "www",
            repoUrl = "wwww",
            forksCount = -1,
            starsCount = -1,
            watchersCount = -1,
        )
}
