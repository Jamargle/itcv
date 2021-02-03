package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.data.network.user.response.search.ResultItem
import com.jmlb0003.itcv.data.network.user.response.search.SearchUserResponse
import com.jmlb0003.itcv.data.repositories.mappers.SearchResultsMappers
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.model.User
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRepositoryTest {

    private val userService = mockk<UserService>(relaxed = true)
    private val usersMapper = mockk<UserMappers>(relaxed = true)
    private val searchResultsMapper = mockk<SearchResultsMappers>(relaxed = true)

    private val repository = UserRepository(userService, usersMapper, searchResultsMapper)

    @Test
    fun `on getUser if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { userService.getUserProfile(any()) } returns Either.Left(error)

        val result = repository.getUser("")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getUser if service returns right user response it converts it to domain model and returns it`() {
        val expectedUsername = "some user name"
        val userResponse = mockk<UserResponse>()
        val expectedUser = mockk<User>()
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)
        every { usersMapper.mapToDomain(userResponse) } returns expectedUser

        val result = repository.getUser(expectedUsername)

        assertEquals(expectedUser, (result as Either.Right).rightValue)
    }

    @Test
    fun `on searchUserByUsername if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { userService.searchUser("") } returns Either.Left(error)

        val result = repository.searchUserByUsername("")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on searchUserByUsername if service returns right user response it converts it to domain model and returns it`() {
        val expectedUsername = "some user name"
        val expectedApiResult = mockk<ResultItem>()
        val expectedDomainResult = mockk<SearchResult>()
        val response = getFakeSearchUserResponse().copy(results = listOf(expectedApiResult))
        every { userService.searchUser(expectedUsername) } returns Either.Right(response)
        every { searchResultsMapper.mapToDomain(expectedApiResult) } returns expectedDomainResult

        val result = repository.searchUserByUsername(expectedUsername)

        assertEquals(expectedDomainResult, (result as Either.Right).rightValue[0])
    }

    private fun getFakeSearchUserResponse() =
        SearchUserResponse(
            totalCount = -1,
            incompleteResults = false,
            results = emptyList()
        )
}
