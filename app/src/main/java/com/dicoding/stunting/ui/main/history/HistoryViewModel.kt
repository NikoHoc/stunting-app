package com.dicoding.stunting.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class HistoryViewModel (private val dataRepository: NourishRepository) : ViewModel() {
    fun getPredictionHistory(userId: String): LiveData<Result<List<PredictionHistoryEntity>>> {
        return dataRepository.getPredictionHistory(userId)
    }
    fun isPredictionInDb(age: Int, height: Float, gender: String, result: String): LiveData<Boolean> {
        return dataRepository.isPredictionExists(age, height, gender, result)
    }
    fun uploadPredict(age: Int, gender: String, height: Float, result: String, description: String) = dataRepository.uploadPredict(age, gender, height, result, description)
}