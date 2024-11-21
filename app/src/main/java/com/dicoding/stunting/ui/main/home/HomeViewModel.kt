package com.dicoding.stunting.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class HomeViewModel (private val dataRepository: NourishRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return dataRepository.getSession().asLiveData()
    }
}