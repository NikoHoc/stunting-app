package com.dicoding.stunting.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stunting.data.remote.DataRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: DataRepository) : ViewModel()  {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}