package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReposRepositoryTest {

    private val reposService = mockk<RepoService>(relaxed = true)

    private val repository = ReposRepository(reposService)

    @Test
    fun `on getUserRepositories if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { reposService.getRepositories(any()) } returns Either.Left(error)

        val result = repository.getUserRepositories("")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getUserRepositories if service returns right repositories response it converts them to domain model and returns it`() {
        val repoResponse1 = getFakeRepoResponse().copy(name = "name1")
        val repoResponse2 = getFakeRepoResponse().copy(name = "name2")
        every { reposService.getRepositories("theUser") } returns Either.Right(listOf(repoResponse1, repoResponse2))

        val result = repository.getUserRepositories("theUser")

        with((result as Either.Right).rightValue[0]) {
            assertEquals("name1", name)
        }
        with(result.rightValue[1]) {
            assertEquals("name2", name)
        }
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
