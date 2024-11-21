package com.dicoding.stunting.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val dataRepository: NourishRepository) : ViewModel() {
    fun saveSession(userModel: UserModel) = viewModelScope.launch {
        dataRepository.saveSession(userModel)
    }
    fun login(email: String, password: String) = dataRepository.login(email, password)
}