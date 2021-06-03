package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.ReposLocalDataSource
import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.domain.model.Repo
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date
import com.jmlb0003.itcv.data.model.Repo as DataRepo

class ReposRepositoryTest {

    private val reposService = mockk<RepoService>(relaxed = true)
    private val reposMappers = mockk<ReposMappers>(relaxed = true)
    private val reposLocalDataSource = mockk<ReposLocalDataSource>(relaxed = true)

    private val repository = ReposRepository(reposLocalDataSource, reposService, reposMappers)

    @Test
    fun `on getUserRepositories returns cached repos if exist and are valid`() {
        val username = "Some username"
        val expectedDataRepo = mockk<DataRepo>()
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Right(listOf(expectedDataRepo))
        val expectedDomainRepo = mockk<Repo>()
        every { expectedDataRepo.lastCacheUpdate } returns Date().time
        every { reposMappers.mapToDomain(expectedDataRepo) } returns expectedDomainRepo

        val result = repository.getUserRepositories(username)

        assertEquals(expectedDomainRepo, (result as Either.Right).rightValue[0])
    }

    @Test
    fun `on getUserRepositories removes cached repos if no longer valid`() {
        val username = "Some username"
        val expectedDataRepo = mockk<DataRepo>()
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Right(listOf(expectedDataRepo))
        every { expectedDataRepo.lastCacheUpdate } returns
                Calendar.getInstance().apply { add(Calendar.HOUR, -25) }.timeInMillis
        every { reposService.getRepositories(username) } returns Either.Right(emptyList())

        repository.getUserRepositories(username)

        verify { reposLocalDataSource.removeReposForUser(username) }
        verify { reposMappers.mapToDomain(any<DataRepo>()) wasNot called }
    }

    @Test
    fun `on getUserRepositories fetches repos from service if no cached repos found`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Right(emptyList())
        val expectedReposResponse = mockk<RepoResponse>()
        every { reposService.getRepositories(username) } returns Either.Right(listOf(expectedReposResponse))
        val expectedDomainRepo = mockk<Repo>()
        every { reposMappers.mapToDomain(expectedReposResponse) } returns expectedDomainRepo

        val result = repository.getUserRepositories(username)

        assertEquals(expectedDomainRepo, (result as Either.Right).rightValue[0])
        verify { reposService.getRepositories(username) }
    }

    @Test
    fun `on getUserRepositories fetches repos from service if error when looking for cached repos`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Left(Failure.NetworkConnection)
        val expectedReposResponse = mockk<RepoResponse>()
        every { reposService.getRepositories(username) } returns Either.Right(listOf(expectedReposResponse))
        val expectedDomainRepo = mockk<Repo>()
        every { reposMappers.mapToDomain(expectedReposResponse) } returns expectedDomainRepo

        val result = repository.getUserRepositories(username)

        assertEquals(expectedDomainRepo, (result as Either.Right).rightValue[0])
        verify { reposService.getRepositories(username) }
    }

    @Test
    fun `on getUserRepositories fetches repos from service if cached repos are not valid`() {
        val username = "Some username"
        val expectedDataRepo = mockk<DataRepo>()
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Right(listOf(expectedDataRepo))
        every { expectedDataRepo.lastCacheUpdate } returns
                Calendar.getInstance().apply { add(Calendar.HOUR, -25) }.timeInMillis
        val expectedReposResponse = mockk<RepoResponse>()
        every { reposService.getRepositories(username) } returns Either.Right(listOf(expectedReposResponse))
        val expectedDomainRepo = mockk<Repo>()
        every { reposMappers.mapToDomain(expectedReposResponse) } returns expectedDomainRepo

        val result = repository.getUserRepositories(username)

        assertEquals(expectedDomainRepo, (result as Either.Right).rightValue[0])
        verify { reposService.getRepositories(username) }
    }

    @Test
    fun `on getUserRepositories without cached repos if service returns failure returns the error`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Left(Failure.NetworkConnection)
        val error = Failure.NetworkConnection
        every { reposService.getRepositories(username) } returns Either.Left(error)

        val result = repository.getUserRepositories(username)

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getUserRepositories without cached repos if service returns error response does nothing else`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Left(Failure.NetworkConnection)
        every { reposService.getRepositories(username) } returns Either.Left(Failure.NetworkConnection)

        repository.getUserRepositories(username)

        verify { reposMappers.mapToData(any(), any(), any()) wasNot called }
        verify { reposLocalDataSource.saveRepos(any()) wasNot called }
    }

    @Test
    fun `on getUserRepositories without cached repos if service returns right repositories response it converts them to domain model and returns it`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Left(Failure.NetworkConnection)
        val repoResponse1 = mockk<RepoResponse>()
        val repo1 = mockk<Repo>()
        val repoResponse2 = mockk<RepoResponse>()
        val repo2 = mockk<Repo>()
        every { reposService.getRepositories(username) } returns Either.Right(listOf(repoResponse1, repoResponse2))
        every { reposMappers.mapToDomain(repoResponse1) } returns repo1
        every { reposMappers.mapToDomain(repoResponse2) } returns repo2

        val result = repository.getUserRepositories(username)

        assertEquals(repo1, (result as Either.Right).rightValue[0])
        assertEquals(repo2, result.rightValue[1])
    }

    @Test
    fun `on getUserRepositories without cached repos if service returns right repositories response caches the new repos`() {
        val username = "Some username"
        every { reposLocalDataSource.getReposByUser(username) } returns Either.Left(Failure.NetworkConnection)
        val repoResponse1 = mockk<RepoResponse>()
        val repo1 = mockk<Repo>()
        val repoResponse2 = mockk<RepoResponse>()
        val repo2 = mockk<Repo>()
        every { reposService.getRepositories(username) } returns Either.Right(listOf(repoResponse1, repoResponse2))
        every { reposMappers.mapToDomain(repoResponse1) } returns repo1
        every { reposMappers.mapToDomain(repoResponse2) } returns repo2
        val dataRepo1 = mockk<DataRepo>()
        val dataRepo2 = mockk<DataRepo>()
        every { reposMappers.mapToData(repo1, username, any()) } returns dataRepo1
        every { reposMappers.mapToData(repo2, username, any()) } returns dataRepo2

        repository.getUserRepositories(username)

        verify { reposLocalDataSource.saveRepos(listOf(dataRepo1, dataRepo2)) }
    }
}
