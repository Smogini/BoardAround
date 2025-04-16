package com.boardaround.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boardaround.data.dao.EventDAO
import com.boardaround.data.dao.PostDao
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.Post
import com.boardaround.data.entities.User
import com.boardaround.data.entities.Friendship

@Database(entities = [User::class, Event::class, Post::class, Friendship::class], version = 3)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun eventDAO(): EventDAO
    abstract fun postDAO(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                instance
            }
        }
    }
}