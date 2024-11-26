package com.dicoding.stunting.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity


@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoJournal(article: List<JournalHistoryEntity>)

    @Query("SELECT * FROM journal")
    fun getAllJournal(): LiveData<List<JournalHistoryEntity>>

    @Query("DELETE FROM journal")
    suspend fun deleteAll()

    @Query("SELECT MAX(created_at) FROM journal")
    fun getLatestJournalTimestamp(): Long?
}