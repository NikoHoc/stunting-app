package com.dicoding.stunting.di

import android.content.Context
import com.dicoding.stunting.data.local.room.NourishDatabase
import com.dicoding.stunting.data.pref.UserPreference
import com.dicoding.stunting.data.pref.dataStore
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import com.dicoding.stunting.data.remote.nourish.retrofit.ApiConfig as NourishApiConfig
import com.dicoding.stunting.data.remote.news.retrofit.ApiConfig as NewsApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideNourishRepository(context: Context): NourishRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = NourishApiConfig.getApiService(user.token)
        return NourishRepository.getInstance(apiService, pref)
    }

    fun providerNewsRepository(context: Context): NewsRepository {
        val apiService = NewsApiConfig.getApiService()
        val database = NourishDatabase.getInstance(context)
        val newsDao = database.newsDao()

        return NewsRepository.getInstance(apiService, newsDao)
    }
}