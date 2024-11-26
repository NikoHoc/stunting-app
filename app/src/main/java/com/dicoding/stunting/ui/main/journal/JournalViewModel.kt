package com.dicoding.stunting.ui.main.journal

import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import java.io.File

class JournalViewModel(private val dataRepository: NourishRepository) : ViewModel() {

    fun uploadJournal(file: File, description: String) = dataRepository.uploadJournal(file, description)

    fun getJournal() = dataRepository.getJournal()
}