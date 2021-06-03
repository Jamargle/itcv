package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.Repo
import com.jmlb0003.itcv.domain.exception.NoInsertedReposException
import com.jmlb0003.itcv.domain.exception.NoReposException
import com.jmlb0003.itcv.domain.exception.NoReposRemovedException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReposLocalDataSourceTest {

    private val database = mockk<MyDataBase>()
    private val reposLocalDataSource = ReposLocalDataSource(database)

    @Test
    fun `on saveRepos returns error if the repos cannot be inserted in the database`() {
        val repo = mockk<Repo>()
        val repos = listOf(repo)
        val expectedError = "Some error message"
        every { database.reposDao().insertRepos(repos) } throws IllegalStateException(expectedError)

        val result = reposLocalDataSource.saveRepos(repos)

        assertTrue((result as Either.Left).leftValue is NoInsertedReposException)
        assertEquals("Not possible to insert repos because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on saveRepos returns Unit from database if properly inserted`() {
        val repo = mockk<Repo>()
        val repos = listOf(repo)
        every { database.reposDao().insertRepos(repos) } returns Unit

        val result = reposLocalDataSource.saveRepos(repos)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }

    @Test
    fun `on getReposByUser returns error if there is a exception thrown by the database`() {
        val username = "SomeUserName"
        val expectedError = "Some error message"
        every { database.reposDao().getReposByUser(username) } throws NullPointerException(expectedError)

        val result = reposLocalDataSource.getReposByUser(username)

        assertTrue((result as Either.Left).leftValue is NoReposException)
        assertTrue(result.leftValue.error is IllegalStateException)
        assertEquals("There are no repos because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on getReposByUser returns the repos from database if found`() {
        val username = "SomeUserName"
        val expectedRepos = mockk<List<Repo>>()
        every { database.reposDao().getReposByUser(username) } returns expectedRepos

        val result = reposLocalDataSource.getReposByUser(username)

        assertEquals(expectedRepos, (result as Either.Right).rightValue)
    }

    @Test
    fun `on removeReposForUser returns error if no repos found in the database`() {
        val username = "SomeUserName"
        every { database.reposDao().getReposByUser(username) } returns emptyList()

        val result = reposLocalDataSource.removeReposForUser(username)

        assertTrue((result as Either.Left).leftValue is NoReposRemovedException)
    }

    @Test
    fun `on removeReposForUser returns error if error when looking for existing repos in the database`() {
        val username = "SomeUserName"
        every { database.reposDao().getReposByUser(username) } throws IllegalStateException()

        val result = reposLocalDataSource.removeReposForUser(username)

        assertTrue((result as Either.Left).leftValue is NoReposRemovedException)
    }

    @Test
    fun `on removeReposForUser returns error if the repos could not be removed from the database`() {
        val username = "SomeUserName"
        val expectedRepos = mockk<List<Repo>>()
        every { database.reposDao().getReposByUser(username) } returns expectedRepos
        every { database.reposDao().removeRepos(expectedRepos) } throws IllegalStateException()

        val result = reposLocalDataSource.removeReposForUser(username)

        assertTrue((result as Either.Left).leftValue is NoReposRemovedException)
    }

    @Test
    fun `on removeReposForUser returns Unit if the repos are successfully removed from database`() {
        val username = "SomeUserName"
        val expectedRepos = mockk<List<Repo>>()
        every { database.reposDao().getReposByUser(username) } returns expectedRepos
        every { database.reposDao().removeRepos(expectedRepos) } returns Unit

        val result = reposLocalDataSource.removeReposForUser(username)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }
}
