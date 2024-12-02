package com.dicoding.stunting.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.stunting.data.remote.news.NewsRepository
import com.dicoding.stunting.data.remote.nourish.NourishRepository
import com.dicoding.stunting.di.Injection
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.main.history.HistoryViewModel
import com.dicoding.stunting.ui.main.journal.JournalViewModel
import com.dicoding.stunting.ui.main.news.NewsViewModel

class ViewModelFactory(
    private val nourishRepository: NourishRepository,
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(nourishRepository) as T
            }
            modelClass.isAssignableFrom(JournalViewModel::class.java) -> {
                JournalViewModel(nourishRepository) as T
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
//        @JvmStatic
//        fun getInstance(context: Context): ViewModelFactory {
//            return INSTANCE ?: synchronized(this) {
//                val nourishRepository = Injection.provideNourishRepository(context)
//                val newsRepository = Injection.providerNewsRepository(context)
//                ViewModelFactory(nourishRepository, newsRepository).also {
//                    INSTANCE = it
//                }
//            }
//        }

        @JvmStatic
        fun getInstance(context: Context) = ViewModelFactory(
            Injection.provideNourishRepository(context),
            Injection.providerNewsRepository(context)
        )


    }
}