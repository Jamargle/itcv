package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import com.jmlb0003.itcv.data.model.User as DataUser
import com.jmlb0003.itcv.domain.model.User as DomainUser

class UserMappersTest {

    private val userMappers = UserMappers

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

        val result = userMappers.mapToDomain(userResponse)

        with(result) {
            assertEquals(expectedUsername, username)
            assertEquals(expectedName, name)
            assertEquals(expectedEmail, email)
            assertEquals(expectedLocation, location)
            assertEquals(expectedRepositoryCount, repositoryCount)
        }
    }

    @Test
    fun `when right user response does not contain name it passes the username`() {
        val expectedUsername = "Expected username"
        val userResponse = getFakeUserResponse().copy(
            username = expectedUsername,
            name = null
        )
        val result = userMappers.mapToDomain(userResponse)
        assertEquals(expectedUsername, result.name)
    }

    @Test
    fun `when right user response does not contain bio it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(bioDescription = null)
        val result = userMappers.mapToDomain(userResponse)
        assertEquals("", result.bio)
    }

    @Test
    fun `when right user response does not contain email it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(email = null)
        val result = userMappers.mapToDomain(userResponse)
        assertEquals("", result.email)
    }

    @Test
    fun `when right user response does not contain location it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(location = null)
        val result = userMappers.mapToDomain(userResponse)
        assertEquals("", result.location)
    }

    @Test
    fun `when right user response does not contain website it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(userWebsite = null)
        val result = userMappers.mapToDomain(userResponse)
        assertEquals("", result.website)
    }

    @Test
    fun `when right user response does not contain twitter account it converts it to empty string`() {
        val userResponse = getFakeUserResponse().copy(twitterAccount = null)
        val result = userMappers.mapToDomain(userResponse)
        assertEquals("", result.twitterAccount)
    }

    @Test
    fun `mapToDomain with data User converts it to a domain model object`() {
        val expectedUsername = "some username"
        val expectedName = "some name"
        val expectedBio = "some bio description"
        val expectedMemberSince = Date(4560000000)
        val expectedEmail = "some email"
        val expectedLocation = "some location"
        val expectedRepositoryCount = 136
        val expectedFollowerCount = 137
        val expectedWebsite = "Some web site"
        val expectedTwitterAccount = "Some twitter user account"
        val dataUser = DataUser(
            userId = expectedUsername,
            name = expectedName,
            bio = expectedBio,
            memberSince = expectedMemberSince,
            email = expectedEmail,
            location = expectedLocation,
            repositoryCount = expectedRepositoryCount,
            followerCount = expectedFollowerCount,
            website = expectedWebsite,
            twitterAccount = expectedTwitterAccount,
            lastCacheUpdate = 0
        )

        val result = userMappers.mapToDomain(dataUser)

        with(result) {
            assertEquals(expectedUsername, username)
            assertEquals(expectedName, name)
            assertEquals(expectedBio, bio)
            assertEquals(expectedMemberSince, memberSince)
            assertEquals(expectedEmail, email)
            assertEquals(expectedLocation, location)
            assertEquals(expectedRepositoryCount, repositoryCount)
            assertEquals(expectedFollowerCount, followerCount)
            assertEquals(expectedWebsite, website)
            assertEquals(expectedTwitterAccount, twitterAccount)
        }
    }

    @Test
    fun `mapToData with domain User converts it to a data model object`() {
        val expectedUsername = "some username"
        val expectedName = "some name"
        val expectedBio = "some bio description"
        val expectedMemberSince = Date(4560000000)
        val expectedEmail = "some email"
        val expectedLocation = "some location"
        val expectedRepositoryCount = 136
        val expectedFollowerCount = 137
        val expectedWebsite = "Some web site"
        val expectedTwitterAccount = "Some twitter user account"
        val expectedUpdatedDate = 4560000000123
        val user = DomainUser(
            username = expectedUsername,
            name = expectedName,
            bio = expectedBio,
            memberSince = expectedMemberSince,
            email = expectedEmail,
            location = expectedLocation,
            repositoryCount = expectedRepositoryCount,
            followerCount = expectedFollowerCount,
            website = expectedWebsite,
            twitterAccount = expectedTwitterAccount
        )

        val result = userMappers.mapToData(user, Date(expectedUpdatedDate))

        with(result) {
            assertEquals(expectedUsername, userId)
            assertEquals(expectedName, name)
            assertEquals(expectedBio, bio)
            assertEquals(expectedMemberSince, memberSince)
            assertEquals(expectedEmail, email)
            assertEquals(expectedLocation, location)
            assertEquals(expectedRepositoryCount, repositoryCount)
            assertEquals(expectedFollowerCount, followerCount)
            assertEquals(expectedWebsite, website)
            assertEquals(expectedTwitterAccount, twitterAccount)
            assertEquals(expectedUpdatedDate, lastCacheUpdate)
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
