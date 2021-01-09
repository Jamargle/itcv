package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers.mapToDomain
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class UserMappersTest {

    @Test
    fun `mapToDomain converts a user response to a domain model user`() {
        val expectedUsername = "some username"
        val expectedName = "some name"
        val expectedEmail = "some email"
        val expectedLocation = "some location"
        val expectedRepositoryCount = 136
        val expectedFollowerCount = 137
        val expectedFollowingCount = 138
        val expectedDate = Date(4560000000)
        val userResponse = getFakeUserResponse().copy(
            username = expectedUsername,
            name = expectedName,
            email = expectedEmail,
            location = expectedLocation,
            reposCount = expectedRepositoryCount,
            followerCount = expectedFollowerCount,
            followingCount = expectedFollowingCount,
            profileCreatedDate = expectedDate
        )

        val result = mapToDomain(userResponse)

        with(result) {
            assertEquals(expectedUsername, username)
            assertEquals(expectedName, name)
            assertEquals(expectedEmail, email)
            assertEquals(expectedLocation, location)
            assertEquals(expectedRepositoryCount, repositoryCount)
        }
    }

    @Test
    fun `when right user response does not contain email it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(email = null)
        val result = mapToDomain(userResponse)
        assertEquals("", result.email)
    }

    @Test
    fun `when right user response does not contain location it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(location = null)
        val result = mapToDomain(userResponse)
        assertEquals("", result.location)
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
