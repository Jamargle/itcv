package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.domain.model.Repo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReposRepositoryTest {

    private val reposService = mockk<RepoService>(relaxed = true)
    private val reposMappers = mockk<ReposMappers>(relaxed = true)

    private val repository = ReposRepository(reposService, reposMappers)

    @Test
    fun `on getUserRepositories if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { reposService.getRepositories(any()) } returns Either.Left(error)

        val result = repository.getUserRepositories("")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getUserRepositories if service returns right repositories response it converts them to domain model and returns it`() {
        val repoResponse1 = mockk<RepoResponse>()
        val repo1 = mockk<Repo>()
        val repoResponse2 = mockk<RepoResponse>()
        val repo2 = mockk<Repo>()
        every { reposService.getRepositories("theUser") } returns Either.Right(listOf(repoResponse1, repoResponse2))
        every { reposMappers.mapToDomain(repoResponse1) } returns repo1
        every { reposMappers.mapToDomain(repoResponse2) } returns repo2

        val result = repository.getUserRepositories("theUser")

        assertEquals(repo1, (result as Either.Right).rightValue[0])
        assertEquals(repo2, result.rightValue[1])
    }
}
