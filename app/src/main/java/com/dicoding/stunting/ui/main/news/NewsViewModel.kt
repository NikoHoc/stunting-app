package com.dicoding.stunting.ui.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.Result

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getNews(): LiveData<Result<List<NewsEntity>>> {
        return newsRepository.getNews()
    }
}