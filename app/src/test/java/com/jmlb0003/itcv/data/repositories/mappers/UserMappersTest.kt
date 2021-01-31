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
        val expectedDate = Date(4560000000)
        val userResponse = getFakeUserResponse().copy(
            username = expectedUsername,
            name = expectedName,
            email = expectedEmail,
            location = expectedLocation,
            reposCount = expectedRepositoryCount,
            followerCount = expectedFollowerCount,
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
    fun `when right user response does not contain bio it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(bioDescription = null)
        val result = mapToDomain(userResponse)
        assertEquals("", result.bio)
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

    @Test
    fun `when right user response does not contain website it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(userWebsite = null)
        val result = mapToDomain(userResponse)
        assertEquals("", result.website)
    }

    @Test
    fun `when right user response does not contain twitter account it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(twitterAccount = null)
        val result = mapToDomain(userResponse)
        assertEquals("", result.twitterAccount)
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
