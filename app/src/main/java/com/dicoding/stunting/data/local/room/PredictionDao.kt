package com.dicoding.stunting.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity

@Dao
interface PredictionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoPredictions(prediction: List<PredictionHistoryEntity>)

    @Query("SELECT * FROM prediction")
    fun getAllPrediction(): LiveData<List<PredictionHistoryEntity>>

    @Query("DELETE FROM prediction")
    suspend fun deleteAll()

//    @Query("SELECT MAX(created_at) FROM prediction")
//    fun getLatestNewsTimestamp(): Long?
}