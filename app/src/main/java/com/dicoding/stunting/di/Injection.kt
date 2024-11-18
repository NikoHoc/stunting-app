package com.dicoding.stunting.di

import android.content.Context
import com.dicoding.stunting.data.pref.UserPreference
import com.dicoding.stunting.data.pref.dataStore
import com.dicoding.stunting.data.remote.DataRepository
import com.dicoding.stunting.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return DataRepository.getInstance(apiService, pref)
    }
}