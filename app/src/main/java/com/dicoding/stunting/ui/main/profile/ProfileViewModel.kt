package com.dicoding.stunting.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val dataRepository: NourishRepository) : ViewModel()  {
    fun getSession() = dataRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            dataRepository.logout()
        }
    }
}