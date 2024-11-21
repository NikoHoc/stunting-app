package com.dicoding.stunting.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class MainViewModel(private val dataRepository: NourishRepository): ViewModel() {
    fun getSession() = dataRepository.getSession().asLiveData()
}