package com.jmlb0003.itcv.data.local.database

import androidx.room.TypeConverter
import java.util.Date

class DateRoomConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time
}
