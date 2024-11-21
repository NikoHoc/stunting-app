package com.dicoding.stunting.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import kotlinx.coroutines.launch

class AuthenticationViewModel (private val dataRepository: NourishRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = dataRepository.registerUser(name, email, password)

    fun saveSession(userModel: UserModel) = viewModelScope.launch {
        dataRepository.saveSession(userModel)
    }

    fun getSession() = dataRepository.getSession().asLiveData()

    fun login(email: String, password: String) = dataRepository.login(email, password)

    fun logout() {
        viewModelScope.launch {
            dataRepository.logout()
        }
    }
}