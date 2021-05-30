package com.jmlb0003.itcv.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jmlb0003.itcv.data.model.User

private const val DATABASE_NAME = "itcv.db"
internal const val USER_TABLE_NAME = "users"

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateRoomConverters::class)
abstract class MyDataBase : RoomDatabase() {

    internal abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: MyDataBase? = null

        fun getInstance(context: Context): MyDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDataBase::class.java,
                DATABASE_NAME
            ).build()
    }
}
