package com.jmlb0003.itcv.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jmlb0003.itcv.data.local.database.REPO_TABLE_NAME

internal const val REPO_OWNER_ID = "repo_owner_id"
internal const val REPO_ID = "repo_id"

@Entity(
    tableName = REPO_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [USER_ID],
            childColumns = [REPO_OWNER_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [REPO_OWNER_ID])]
)
class Repo(
    @PrimaryKey
    @ColumnInfo(name = REPO_ID)
    val id: String,
    val name: String,
    val description: String,
    val website: String,
    val repoUrl: String,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    @ColumnInfo(name = REPO_OWNER_ID) val owner: String,
    val lastCacheUpdate: Long
)
