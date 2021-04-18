package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.MissingDefaultUserNameFailure
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class UserRepositoryTest {

    private val userService = mockk<UserService>(relaxed = true)
    private val sharedPreferences = mockk<SharedPreferencesHandler>(relaxed = true)

    private val repository = UserRepository(sharedPreferences, userService)

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
        val error = Failure.NetworkConnection
        every { userService.getUserProfile(expectedUsername) } returns Either.Left(error)

        val result = repository.getDefaultUser()

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getDefaultUser if sharedPreferences returns not empty then returns the user if service returns an user`() {
        val expectedUsername = "some user name"
        every { sharedPreferences.defaultUserName } returns expectedUsername
        val userResponse = getFakeUserResponse().copy(username = expectedUsername)
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)

        val result = repository.getDefaultUser()

        with((result as Either.Right).rightValue) {
            assertEquals(expectedUsername, username)
            assertEquals("zzz", name)
            assertEquals("zzz", email)
            assertEquals("zzz", location)
            assertEquals(-1, repositoryCount)
        }
    }

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
        val userResponse = getFakeUserResponse().copy(username = expectedUsername)
        every { userService.getUserProfile(expectedUsername) } returns Either.Right(userResponse)

        val result = repository.getUser(expectedUsername)

        with((result as Either.Right).rightValue) {
            assertEquals(expectedUsername, username)
            assertEquals("zzz", name)
            assertEquals("zzz", email)
            assertEquals("zzz", location)
            assertEquals(-1, repositoryCount)
        }
    }

    private fun getFakeUserResponse() =
        UserResponse(
            username = "zzz",
            name = "zzz",
            bioDescription = "bbb",
            email = "zzz",
            location = "zzz",
            reposCount = -1,
            followerCount = -1,
            profileCreatedDate = Date(0),
            userWebsite = "www",
            twitterAccount = "ttt"
        )
}
