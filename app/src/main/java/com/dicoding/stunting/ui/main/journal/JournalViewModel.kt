package com.dicoding.stunting.ui.main.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import java.io.File

class JournalViewModel(private val dataRepository: NourishRepository) : ViewModel() {

    fun uploadJournal(file: File, description: String) = dataRepository.uploadJournal(file, description)

    fun getJournal(userId: String): LiveData<Result<List<JournalHistoryEntity>>> {
        return dataRepository.getJournal(userId)
    }
}