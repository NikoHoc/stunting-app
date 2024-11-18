package com.dicoding.stunting.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.stunting.data.remote.DataRepository
import com.dicoding.stunting.di.Injection
import com.dicoding.stunting.ui.history.HistoryViewModel
import com.dicoding.stunting.ui.home.HomeViewModel
import com.dicoding.stunting.ui.login.LoginViewModel
import com.dicoding.stunting.ui.profile.ProfileViewModel
import com.dicoding.stunting.ui.register.RegisterViewModel
import com.dicoding.stunting.ui.splash.SplashViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}