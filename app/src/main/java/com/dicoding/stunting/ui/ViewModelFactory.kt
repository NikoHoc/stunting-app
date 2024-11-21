package com.dicoding.stunting.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import com.dicoding.stunting.di.Injection
import com.dicoding.stunting.ui.main.history.HistoryViewModel
import com.dicoding.stunting.ui.main.home.HomeViewModel
import com.dicoding.stunting.ui.authentication.login.LoginViewModel
import com.dicoding.stunting.ui.main.profile.ProfileViewModel
import com.dicoding.stunting.ui.authentication.register.RegisterViewModel
import com.dicoding.stunting.ui.main.MainViewModel
import com.dicoding.stunting.ui.main.home.NewsViewModel
import com.dicoding.stunting.ui.splash.SplashViewModel

class ViewModelFactory(
    private val nourishRepository: NourishRepository,
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(newsRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
//        @JvmStatic
//        fun getInstance(context: Context) = ViewModelFactory(Injection.provideNourishRepository(context))
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val nourishRepository = Injection.provideNourishRepository(context)
                val newsRepository = Injection.providerNewsRepository(context)
                ViewModelFactory(nourishRepository, newsRepository).also {
                    INSTANCE = it
                }
            }
        }


    }
}