package com.dicoding.stunting.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.NewsEntity

@Database(entities = [NewsEntity::class, JournalHistoryEntity::class], version = 1, exportSchema = false)
abstract class NourishDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var instance: NourishDatabase? = null
        fun getInstance(context: Context): NourishDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NourishDatabase::class.java, "nourish.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}