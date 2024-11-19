package com.dicoding.stunting.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.stunting.data.remote.DataRepository

class MainViewModel(private val dataRepository: DataRepository): ViewModel() {
    fun getSession() = dataRepository.getSession().asLiveData()
}