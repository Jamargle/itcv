package com.jmlb0003.itcv.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmlb0003.itcv.data.model.REPO_OWNER_ID
import com.jmlb0003.itcv.data.model.Repo

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<Repo>)

    @Delete
    fun removeRepos(repos: List<Repo>)

    @Transaction
    @Query(
        "SELECT * FROM $REPO_TABLE_NAME" +
                " WHERE $REPO_OWNER_ID LIKE :username"
    )
    fun getReposByUser(username: String): List<Repo>
}
