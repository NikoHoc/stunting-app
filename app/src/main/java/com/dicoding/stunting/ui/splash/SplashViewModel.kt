package com.dicoding.stunting.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.stunting.data.remote.DataRepository

class SplashViewModel(private val dataRepository: DataRepository): ViewModel() {
    fun getSession() = dataRepository.getSession().asLiveData()
}