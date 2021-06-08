package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import com.jmlb0003.itcv.domain.model.Repo
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import com.jmlb0003.itcv.data.model.Repo as DataRepo

class ReposMappersTest {

    private val reposMappers = ReposMappers

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

        val result = reposMappers.mapToDomain(repoResponse)

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
        val result = reposMappers.mapToDomain(repoResponse)
        assertEquals("", result.description)
    }

    @Test
    fun `when right repo response does not contain websiteUrl it converts it to empty string`() {
        val repoResponse = getFakeRepoResponse().copy(websiteUrl = null)
        val result = reposMappers.mapToDomain(repoResponse)
        assertEquals("", result.website)
    }

    @Test
    fun `when right repo response does not contain repoUrl it converts it to empty string`() {
        val repoResponse = getFakeRepoResponse().copy(repoUrl = null)
        val result = reposMappers.mapToDomain(repoResponse)
        assertEquals("", result.repoUrl)
    }

    @Test
    fun `mapToDomain converts a DataRepo to a domain model repo`() {
        val expectedId = "some id"
        val expectedName = "some name"
        val expectedDescription = "some description"
        val expectedWebsiteUrl = "some website"
        val expectedRepoUrl = "some github url"
        val expectedForkCount = 12
        val expectedStarsCount = 34
        val expectedWatchersCount = 56
        val dataRepo = DataRepo(
            id = expectedId,
            name = expectedName,
            description = expectedDescription,
            website = expectedWebsiteUrl,
            repoUrl = expectedRepoUrl,
            forksCount = expectedForkCount,
            starsCount = expectedStarsCount,
            watchersCount = expectedWatchersCount,
            owner = "Some username",
            lastCacheUpdate = 0
        )

        val result = reposMappers.mapToDomain(dataRepo)

        with(result) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedWebsiteUrl, website)
            assertEquals(expectedRepoUrl, repoUrl)
            assertEquals(expectedForkCount, forksCount)
            assertEquals(expectedStarsCount, starsCount)
            assertEquals(expectedWatchersCount, watchersCount)
        }
    }

    @Test
    fun `mapToData with domain Repo converts it to a data model object`() {
        val expectedId = "some id"
        val expectedName = "some name"
        val expectedDescription = "some description"
        val expectedWebsiteUrl = "some website"
        val expectedRepoUrl = "some github url"
        val expectedForkCount = 12
        val expectedStarsCount = 34
        val expectedWatchersCount = 56
        val expectedUpdatedDate = 4560000000123
        val expectedRepoOwner = "Some username"
        val repo = Repo(
            id = expectedId,
            name = expectedName,
            description = expectedDescription,
            website = expectedWebsiteUrl,
            repoUrl = expectedRepoUrl,
            forksCount = expectedForkCount,
            starsCount = expectedStarsCount,
            watchersCount = expectedWatchersCount
        )

        val result = reposMappers.mapToData(
            repo,
            expectedRepoOwner,
            Date(expectedUpdatedDate)
        )

        with(result) {
            assertEquals(expectedId, id)
            assertEquals(expectedName, name)
            assertEquals(expectedDescription, description)
            assertEquals(expectedWebsiteUrl, website)
            assertEquals(expectedRepoUrl, repoUrl)
            assertEquals(expectedForkCount, forksCount)
            assertEquals(expectedStarsCount, starsCount)
            assertEquals(expectedWatchersCount, watchersCount)
            assertEquals(expectedRepoOwner, owner)
            assertEquals(expectedUpdatedDate, lastCacheUpdate)
        }
    }

    private fun getFakeRepoResponse() =
        RepoResponse(
            id = "zzz",
            name = "zzz",
            description = "ddd",
            websiteUrl = "www",
            repoUrl = "wwww",
            forksCount = -1,
            starsCount = -1,
            watchersCount = -1,
        )
}
