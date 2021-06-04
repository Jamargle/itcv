package com.jmlb0003.itcv.data.local.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.jmlb0003.itcv.data.model.Repo
import com.jmlb0003.itcv.data.model.Topic
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
    private lateinit var topicsDao: TopicsDao
    private lateinit var db: MyDataBase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MyDataBase::class.java
        ).build()
        userDao = db.userDao()
        reposDao = db.reposDao()
        topicsDao = db.topicsDao()
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

    @Test
    fun writeReadAndRemoveTopics() {
        val topicName1 = "Topic 1"
        val topicName2 = "Topic 2"
        val topicName3 = "Topic 3"
        val repoId1 = "Repo123abc"
        val repoId2 = "Repo456def"
        val lastUpdate = 123456789L
        val topic1 = Topic(
            name = topicName1,
            relatedRepo = repoId1,
            lastCacheUpdate = lastUpdate
        )
        val topic2 = Topic(
            name = topicName2,
            relatedRepo = repoId2,
            lastCacheUpdate = lastUpdate
        )
        val topic3 = Topic(
            name = topicName3,
            relatedRepo = repoId2,
            lastCacheUpdate = lastUpdate
        )

        val ownerUsername = "SomeUsername"
        userDao.insertUser(getFakeUser(userId = ownerUsername))
        reposDao.insertRepos(
            listOf(
                getFakeRepo(expectedId = repoId1, expectedOwner = ownerUsername),
                getFakeRepo(expectedId = repoId2, expectedOwner = ownerUsername)
            )
        )

        topicsDao.insertTopics(listOf(topic1, topic2, topic3))

        val insertedTopics1 = topicsDao.getTopicsByRepo(repoId1)
        assertEquals(1, insertedTopics1.size)
        with(insertedTopics1[0]) {
            assertEquals(topicName1, name)
            assertEquals(repoId1, relatedRepo)
            assertEquals(lastUpdate, lastCacheUpdate)
        }
        val insertedTopics2 = topicsDao.getTopicsByRepo(repoId2)
        assertEquals(2, insertedTopics2.size)
        with(insertedTopics2[0]) {
            assertEquals(topicName2, name)
            assertEquals(repoId2, relatedRepo)
            assertEquals(lastUpdate, lastCacheUpdate)
        }
        with(insertedTopics2[1]) {
            assertEquals(topicName3, name)
            assertEquals(repoId2, relatedRepo)
            assertEquals(lastUpdate, lastCacheUpdate)
        }

        topicsDao.getTopicsByRepo(repoId1).let { topicsDao.removeTopics(it) }
        topicsDao.getTopicsByRepo(repoId2).let { topicsDao.removeTopics(it) }
        assertEquals(emptyList<Topic>(), topicsDao.getTopicsByRepo(repoId1))
        assertEquals(emptyList<Topic>(), topicsDao.getTopicsByRepo(repoId2))
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
