package com.dicoding.stunting.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.remote.DataRepository

class RegisterViewModel(private val repository: DataRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.registerUser(name, email, password)

}