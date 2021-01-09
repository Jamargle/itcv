package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class UserRepositoryTest {

    private val userService = mockk<UserService>(relaxed = true)

    private val repository = UserRepository(userService)

    @Test
    fun `on getUser if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { userService.getUserProfile(any()) } returns Either.Left(error)

        val result = repository.getUser("")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getUser if service returns right user response it converts it to domain model and returns it`() {
        val userResponse = getFakeUserResponse()
        every { userService.getUserProfile(any()) } returns Either.Right(userResponse)

        val result = repository.getUser("")

        with((result as Either.Right).rightValue) {
            assertEquals("zzz", username)
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
            email = "zzz",
            location = "zzz",
            reposCount = -1,
            followerCount = -1,
            followingCount = -1,
            profileCreatedDate = Date(0)
        )
}
