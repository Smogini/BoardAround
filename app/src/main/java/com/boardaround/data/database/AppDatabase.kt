package com.boardaround.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boardaround.data.dao.AchievementDAO
import com.boardaround.data.dao.EventDAO
import com.boardaround.data.dao.GameDAO
import com.boardaround.data.dao.PostDao
import com.boardaround.data.dao.UserDAO
import com.boardaround.data.entities.Achievement
import com.boardaround.data.entities.Event
import com.boardaround.data.entities.Friendship
import com.boardaround.data.entities.Post
import com.boardaround.data.entities.SavedGame
import com.boardaround.data.entities.User

@Database(
    entities = [
        User::class, Event::class, Post::class,
        Friendship::class, SavedGame::class, Achievement::class
        ],
    version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun eventDAO(): EventDAO
    abstract fun postDAO(): PostDao
    abstract fun gameDAO(): GameDAO
    abstract fun achievementDAO(): AchievementDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "boardaround_db"
                ).fallbackToDestructiveMigration(true).build()

                INSTANCE = instance

                instance
            }
        }
    }
}