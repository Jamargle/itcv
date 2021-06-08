package com.jmlb0003.itcv.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmlb0003.itcv.data.model.RELATED_REPO_ID
import com.jmlb0003.itcv.data.model.Topic

@Dao
interface TopicsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topics: List<Topic>)

    @Delete
    fun removeTopics(topics: List<Topic>)

    @Transaction
    @Query(
        "SELECT * FROM $TOPIC_TABLE_NAME" +
                " WHERE $RELATED_REPO_ID LIKE :repoId"
    )
    fun getTopicsByRepo(repoId: String): List<Topic>
}
