package com.dicoding.stunting.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.Result

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

//    private var _cachedNews: LiveData<Result<List<NewsEntity>>>? = null
//
//    fun getNews(): LiveData<Result<List<NewsEntity>>> {
//        if (_cachedNews == null) {
//            _cachedNews = newsRepository.getNews()
//        }
//        return _cachedNews!!
//    }

    fun getNews(): LiveData<Result<List<NewsEntity>>> {
        return newsRepository.getNews()
    }
}