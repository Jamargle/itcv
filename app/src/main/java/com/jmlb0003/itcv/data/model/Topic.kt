package com.jmlb0003.itcv.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.jmlb0003.itcv.data.local.database.TOPIC_TABLE_NAME

internal const val RELATED_REPO_ID = "related_repo_id"
internal const val TOPIC_ID = "topic_id"

@Entity(
    tableName = TOPIC_TABLE_NAME,
    primaryKeys = [
        TOPIC_ID,
        RELATED_REPO_ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = Repo::class,
            parentColumns = [REPO_ID],
            childColumns = [RELATED_REPO_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [RELATED_REPO_ID])]
)
class Topic(
    @ColumnInfo(name = TOPIC_ID) val name: String,
    @ColumnInfo(name = RELATED_REPO_ID) val relatedRepo: String,
    val lastCacheUpdate: Long
)
