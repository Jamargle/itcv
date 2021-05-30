package com.jmlb0003.itcv.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmlb0003.itcv.data.model.USER_ID
import com.jmlb0003.itcv.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun removeUser(user: User)

    @Transaction
    @Query(
        "SELECT * FROM $USER_TABLE_NAME" +
                " WHERE $USER_ID LIKE :username"
    )
    fun getUser(username: String): User?
}
