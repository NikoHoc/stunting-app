package com.dicoding.stunting.ui.main.stunting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class StuntingViewModel (private val dataRepository: NourishRepository) : ViewModel() {
    fun isPredictionInDb(age: Int, height: Float, gender: String, result: String): LiveData<Boolean> {
        return dataRepository.isPredictionExists(age, height, gender, result)
    }
    fun uploadPredict(age: Int, gender: String, height: Float, result: String, description: String) = dataRepository.uploadPredict(age, gender, height, result, description)

    fun getFoodRecommendation(classification: Int) = dataRepository.getFoodRecommendation(classification)
}