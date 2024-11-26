package com.dicoding.stunting.di

import android.content.Context
import com.dicoding.stunting.data.local.room.NourishDatabase
import com.dicoding.stunting.data.local.pref.UserPreference
import com.dicoding.stunting.data.local.pref.dataStore
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import com.dicoding.stunting.data.remote.nourish.retrofit.NourishApiConfig as NourishApiConfig
import com.dicoding.stunting.data.remote.news.retrofit.NewsApiConfig as NewsApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideNourishRepository(context: Context): NourishRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = NourishApiConfig.getApiService(user.token)
        val database = NourishDatabase.getInstance(context)
        val journalDao = database.journalDao()
        return NourishRepository.getInstance(apiService, pref, journalDao)
    }

    fun providerNewsRepository(context: Context): NewsRepository {
        val apiService = NewsApiConfig.getApiService()
        val database = NourishDatabase.getInstance(context)
        val newsDao = database.newsDao()

        return NewsRepository.getInstance(apiService, newsDao)
    }
}
