package com.dicoding.stunting.ui.authentication.register

import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.remote.nourish.NourishRepository

class RegisterViewModel(private val dataRepository: NourishRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = dataRepository.registerUser(name, email, password)

}