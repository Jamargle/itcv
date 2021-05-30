package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.MissingDefaultUserNameFailure
import com.jmlb0003.itcv.data.local.UserLocalDataSource
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.data.network.user.response.search.ResultItem
import com.jmlb0003.itcv.data.network.user.response.search.SearchUserResponse
import com.jmlb0003.itcv.data.repositories.mappers.SearchResultsMappers
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.model.User
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.jmlb0003.itcv.data.model.User as DataUser

class UserRepositoryTest {

    private val userLocalDataSource = mockk<UserLocalDataSource>(relaxed = true)
    private val userService = mockk<UserService>(relaxed = true)
    private val sharedPreferences = mockk<SharedPreferencesHandler>(relaxed = true)
    private val usersMapper = mockk<UserMappers>(relaxed = true)
    private val searchResultsMapper = mockk<SearchResultsMappers>(relaxed = true)

    private val repository = UserRepository(
        sharedPreferences,
        userLocalDataSource,
        userService,
        usersMapper,
        searchResultsMapper
    )

    @Test
    fun `on updateDefaultUser sets given username as default and return Right result`() {
        val newUser = "Some username"
        val result = repository.updateDefaultUser(newUser)

        verify { sharedPreferences.defaultUserName = newUser }
        assertTrue(result is Either.Right)
    }

    @Test
    fun `on getDefaultUser if sharedPreferences returns empty then returns MissingDefaultUserNameFailure`() {
        every { sharedPreferences.defaultUserName } returns ""

        val result = repository.getDefaultUser()

        verify { userService wasNot called }
        assertEquals(MissingDefaultUserNameFailure, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getDefaultUser if sharedPreferences returns not empty then returns error if service returns error`() {
        val expectedUsername = "some user name"
        every { sharedPreferences.defaultUserName } returns expectedUsername
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Left(mockk())
        val error = Failure.NetworkConnection
        every { userService.getUserProfile(expectedUsername) } returns Either.Left(error)

        val result = repository.getDefaultUser()

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getDefaultUser if sharedPreferences returns not empty then returns the user if service returns an user`() {
        val expectedUsername = "some user name"
        every { sharedPreferences.defaultUserName } returns expectedUsername
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Left(mockk())
        val userResponse = mockk<UserResponse>()
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)
        val expectedUser = mockk<User>()
        every { usersMapper.mapToDomain(userResponse) } returns expectedUser

        val result = repository.getDefaultUser()

        assertEquals(expectedUser, (result as Either.Right).rightValue)
    }

    @Test
    fun `on getUser returns cached user if local source returns a valid cached user`() {
        val expectedUsername = "some user name"
        val expectedDataUser = mockk<DataUser>()
        val expectedUser = mockk<User>()
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Right(expectedDataUser)
        every { usersMapper.mapToDomain(expectedDataUser) } returns expectedUser

        val result = repository.getUser(expectedUsername)

        assertEquals(expectedUser, (result as Either.Right).rightValue)
        verify { userService wasNot called }
    }

    @Test
    fun `on getUser returns user from remote if local source returns a non valid cached user`() {
        val expectedUsername = "some user name"
        val expectedDataUser = mockk<DataUser>()
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Right(expectedDataUser)
        val otherPossibleUser = mockk<User>()
        every { usersMapper.mapToDomain(expectedDataUser) } returns otherPossibleUser
        val userResponse = mockk<UserResponse>()
        val expectedUser = mockk<User>()
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)
        every { usersMapper.mapToDomain(userResponse) } returns expectedUser

        val result = repository.getUser(expectedUsername)

        assertEquals(expectedUser, (result as Either.Right).rightValue)
        verify(exactly = 0) { usersMapper.mapToDomain(any<DataUser>()) }
    }

    @Test
    fun `on getUser returns user from remote if local source returns failure`() {
        val expectedUsername = "some user name"
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Left(Failure.NetworkConnection)
        val userResponse = mockk<UserResponse>()
        val expectedUser = mockk<User>()
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)
        every { usersMapper.mapToDomain(userResponse) } returns expectedUser

        val result = repository.getUser(expectedUsername)

        assertEquals(expectedUser, (result as Either.Right).rightValue)
        verify(exactly = 0) { usersMapper.mapToDomain(any<DataUser>()) }
    }

    @Test
    fun `on getUser if service returns failure returns the error`() {
        every { userLocalDataSource.getUser(any()) } returns Either.Left(mockk())
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
        every { userLocalDataSource.getUser(expectedUsername) } returns Either.Left(mockk())
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
