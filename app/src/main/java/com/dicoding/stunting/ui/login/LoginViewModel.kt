package com.dicoding.stunting.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.DataRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    fun saveSession(userModel: UserModel) = viewModelScope.launch {
        repository.saveSession(userModel)
    }
    fun login(email: String, password: String) = repository.login(email, password)
}