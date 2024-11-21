package com.dicoding.stunting.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class SplashViewModel(private val dataRepository: NourishRepository): ViewModel() {
    fun getSession() = dataRepository.getSession().asLiveData()
}