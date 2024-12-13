package com.dicoding.stunting.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity

@Dao
interface PredictionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoPredictions(prediction: List<PredictionHistoryEntity>)

    @Query("SELECT * FROM prediction")
    fun getAllPrediction(): LiveData<List<PredictionHistoryEntity>>

    @Query("SELECT * FROM prediction WHERE user_id = :userId")
    fun getPredictionsByUserId(userId: String): LiveData<List<PredictionHistoryEntity>>

    @Query("DELETE FROM prediction")
    suspend fun deleteAll()

    @Query(
        "SELECT EXISTS(SELECT * FROM prediction WHERE age = :age AND height = :height AND gender = :gender AND result = :result)"
    )
    fun isPredictionExists(age: Int, height: Float, gender: String, result: String): LiveData<Boolean>

}