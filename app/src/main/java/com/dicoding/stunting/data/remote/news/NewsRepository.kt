package com.dicoding.stunting.data.remote.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.stunting.BuildConfig
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.local.room.NewsDao
import com.dicoding.stunting.data.remote.news.retrofit.ApiService
import com.dicoding.stunting.data.remote.Result
import kotlin.math.log

class NewsRepository private constructor(
    private val newsApiService: ApiService,
    private val newsDao: NewsDao
) {

    fun getNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        emit(Result.Loading)

        val localData: LiveData<Result<List<NewsEntity>>> = newsDao.getAllNews().map { newsList ->
            Log.d("NewsRepository", "Fetched news from DB: ${newsList.size}")
            Result.Success(newsList)
        }

        val localResult = localData.value
        if (localResult is Result.Success && localResult.data.isEmpty()) {
            try {
                val response = newsApiService.getStuntingNews(apiKey = BuildConfig.NEWS_API_KEY)
                val listNews = response.articles?.map { news ->
                    NewsEntity(
                        title = news?.title,
                        url = news?.url,
                        urlToImage = news?.urlToImage
                    )
                }
                newsDao.deleteAll()
                Log.d("NewsRepository", "Old news deleted")
                newsDao.insertIntoNews(listNews!!)
                Log.d("NewsRepository", "New news inserted into database: ${listNews.size}")
            } catch (e: Exception) {
                Log.d("NewsRepository", "getNewsArticle: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
                return@liveData
            }
        }

        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            newsApiService: ApiService,
            newsDao: NewsDao,

        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(
                    newsApiService,
                    newsDao,
                )
            }.also { instance = it }
    }
}