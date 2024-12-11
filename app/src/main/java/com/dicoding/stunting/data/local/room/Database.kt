package com.dicoding.stunting.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity

@Database(entities = [NewsEntity::class, JournalHistoryEntity::class, PredictionHistoryEntity::class], version = 1, exportSchema = false)
abstract class NourishDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun journalDao(): JournalDao
    abstract fun predictionDao(): PredictionDao

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