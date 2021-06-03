package com.jmlb0003.itcv.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jmlb0003.itcv.data.local.database.USER_TABLE_NAME
import java.util.Date

internal const val USER_ID = "user_id"

@Entity(
    tableName = USER_TABLE_NAME
)
class User(
    @PrimaryKey
    @ColumnInfo(name = USER_ID)
    val userId: String, // TODO for testing database model upgrades: try adding actual user ID since the username can be changed by user
    val name: String,
    val bio: String,
    val memberSince: Date,
    val email: String,
    val location: String,
    val repositoryCount: Int,
    val followerCount: Int,
    val website: String,
    val twitterAccount: String,
    var lastCacheUpdate: Long
)
