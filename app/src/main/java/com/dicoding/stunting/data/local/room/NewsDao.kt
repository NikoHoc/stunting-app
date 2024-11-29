package com.dicoding.stunting.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.stunting.data.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoNews(article: List<NewsEntity>)

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<NewsEntity>>

    @Query("DELETE FROM news")
    suspend fun deleteAll()

    @Query("SELECT MAX(created_at) FROM news")
    fun getLatestNewsTimestamp(): Long?
}