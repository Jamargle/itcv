package com.jmlb0003.itcv.data.local.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.jmlb0003.itcv.data.model.Repo
import com.jmlb0003.itcv.data.model.User
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class MyDataBaseTest {

    private lateinit var userDao: UserDao
    private lateinit var reposDao: ReposDao
    private lateinit var db: MyDataBase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MyDataBase::class.java
        ).build()
        userDao = db.userDao()
        reposDao = db.reposDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeReadAndRemoveUsers() {
        val username1 = "User 1"
        val username1MemberSinceTime = 12345678900
        val userToInsert1 = getFakeUser(
            userId = username1,
            memberSince = Date(username1MemberSinceTime)
        )
        val username2 = "User 2"
        val userToInsert2 = getFakeUser(username2)

        userDao.insertUser(userToInsert1)
        userDao.insertUser(userToInsert2)

        assertEquals(username1, userDao.getUser(username1)?.userId)
        assertEquals(username1MemberSinceTime, userDao.getUser(username1)?.memberSince?.time)
        assertEquals(username2, userDao.getUser(username2)?.userId)

        userDao.getUser(username1)?.let { userDao.removeUser(it) }
        assertNull(userDao.getUser(username1))
        userDao.getUser(username2)?.let { userDao.removeUser(it) }
        assertNull(userDao.getUser(username2))
    }

    @Test
    fun writeReadAndRemoveRepos() {
        val repoId = "Repo123abc"
        val repoName = "Repo 123 abc"
        val username = "User 1"
        val lastUpdate = 123456789L
        val reposToInsert = getFakeRepo(
            expectedId = repoId,
            expectedName = repoName,
            expectedOwner = username,
            expectedLastUpdate = lastUpdate
        )

        userDao.insertUser(getFakeUser(userId = username))

        reposDao.insertRepos(listOf(reposToInsert))

        val insertedRepo = reposDao.getReposByUser(username)[0]
        with(insertedRepo) {
            assertEquals(repoId, id)
            assertEquals(repoName, name)
            assertEquals(username, owner)
            assertEquals(lastUpdate, lastCacheUpdate)
        }

        reposDao.getReposByUser(username).let { reposDao.removeRepos(it) }
        assertEquals(emptyList<Repo>(), reposDao.getReposByUser(username))
    }

    private fun getFakeUser(
        userId: String = "",
        name: String = "",
        bio: String = "",
        memberSince: Date = Date(),
        email: String = "",
        location: String = "",
        repositoryCount: Int = -1,
        followerCount: Int = -1,
        website: String = "",
        twitterAccount: String = "",
        lastCacheUpdate: Long = -1
    ) = User(
        userId = userId,
        name = name,
        bio = bio,
        memberSince = memberSince,
        email = email,
        location = location,
        repositoryCount = repositoryCount,
        followerCount = followerCount,
        website = website,
        twitterAccount = twitterAccount,
        lastCacheUpdate = lastCacheUpdate
    )

    private fun getFakeRepo(
        expectedId: String = "",
        expectedName: String = "",
        expectedDescription: String = "",
        expectedWebsiteUrl: String = "",
        expectedRepoUrl: String = "",
        expectedForkCount: Int = -1,
        expectedStarsCount: Int = -1,
        expectedWatchersCount: Int = -1,
        expectedOwner: String = "",
        expectedLastUpdate: Long = -1
    ) = Repo(
        id = expectedId,
        name = expectedName,
        description = expectedDescription,
        website = expectedWebsiteUrl,
        repoUrl = expectedRepoUrl,
        forksCount = expectedForkCount,
        starsCount = expectedStarsCount,
        watchersCount = expectedWatchersCount,
        owner = expectedOwner,
        lastCacheUpdate = expectedLastUpdate
    )
}
