package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.User
import com.jmlb0003.itcv.domain.exception.NoInsertedUserException
import com.jmlb0003.itcv.domain.exception.NoUserException
import com.jmlb0003.itcv.domain.exception.NoUserRemovedException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserLocalDataSourceTest {

    private val database = mockk<MyDataBase>()
    private val userLocalDataSource = UserLocalDataSource(database)

    @Test
    fun `on saveUser returns error if the user cannot be inserted in the database`() {
        val user = mockk<User>()
        val expectedError = "Some error message"
        every { database.userDao().insertUser(user) } throws IllegalStateException(expectedError)

        val result = userLocalDataSource.saveUser(user)

        assertTrue((result as Either.Left).leftValue is NoInsertedUserException)
        assertEquals("Not possible to insert user because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on saveUser returns Unit from database if properly inserted`() {
        val expectedUser = mockk<User>()
        every { database.userDao().insertUser(expectedUser) } returns Unit

        val result = userLocalDataSource.saveUser(expectedUser)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }

    @Test
    fun `on getUser returns error if the user is not found in the database`() {
        val username = "SomeUserName"
        every { database.userDao().getUser(username) } returns null
        val result = userLocalDataSource.getUser(username)

        assertTrue((result as Either.Left).leftValue is NoUserException)
    }

    @Test
    fun `on getUser returns error if there is a exception thrown by the database`() {
        val username = "SomeUserName"
        val expectedError = "Some error message"
        every { database.userDao().getUser(username) } throws NullPointerException(expectedError)

        val result = userLocalDataSource.getUser(username)

        assertTrue((result as Either.Left).leftValue is NoUserException)
        assertTrue(result.leftValue.error is IllegalStateException)
        assertEquals("There is no user because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on getUser returns the user from database if found`() {
        val username = "SomeUserName"
        val expectedUser = mockk<User>()
        every { database.userDao().getUser(username) } returns expectedUser

        val result = userLocalDataSource.getUser(username)

        assertEquals(expectedUser, (result as Either.Right).rightValue)
    }

    @Test
    fun `on removeUser returns error if the user is not found in the database`() {
        val username = "SomeUserName"
        every { database.userDao().getUser(username) } returns null
        val result = userLocalDataSource.removeUser(username)

        assertTrue((result as Either.Left).leftValue is NoUserRemovedException)
    }

    @Test
    fun `on removeUser returns error if the user could not be removed from the database`() {
        val username = "SomeUserName"
        val user = mockk<User>()
        every { database.userDao().getUser(username) } returns user
        every { database.userDao().removeUser(user) } throws IllegalStateException()
        val result = userLocalDataSource.removeUser(username)

        assertTrue((result as Either.Left).leftValue is NoUserRemovedException)
    }

    @Test
    fun `on removeUser returns Unit if the user is successfully removed from database`() {
        val username = "SomeUserName"
        val expectedUser = mockk<User>()
        every { database.userDao().getUser(username) } returns expectedUser
        every { database.userDao().removeUser(expectedUser) } returns Unit

        val result = userLocalDataSource.removeUser(username)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }
}
