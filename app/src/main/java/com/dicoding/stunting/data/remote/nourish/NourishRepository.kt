package com.dicoding.stunting.data.remote.nourish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.room.JournalDao
import com.dicoding.stunting.data.local.pref.UserModel
import com.dicoding.stunting.data.local.pref.UserPreference
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.response.AddJournalResponse
import com.dicoding.stunting.data.remote.nourish.retrofit.NourishApiServices
import com.google.gson.Gson
import com.dicoding.stunting.data.remote.nourish.response.LoginResponse
import com.dicoding.stunting.data.remote.nourish.response.RegisterResponse
import com.dicoding.stunting.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class NourishRepository private constructor(
    private val apiServices: NourishApiServices,
    private val userPreference: UserPreference,
    private val journalDao: JournalDao

) {

    fun uploadJournal(imageFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiServices.uploadStory(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddJournalResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }

    }

//    fun getJournal() = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiServices.getJournal()
//            emit(Result.Success(response))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, JournalResponse::class.java)
//            emit(Result.Error(errorResponse.message.toString()))
//        }
//    }

    fun getJournal(): LiveData<Result<List<JournalHistoryEntity>>> = liveData {
        emit(Result.Loading)

        val isOutdated = withContext(Dispatchers.IO) {
            val currentTime = System.currentTimeMillis()
            journalDao.getLatestJournalTimestamp()?.let { lastUpdated ->
                val timeDifference = currentTime - lastUpdated
                Log.d("NourishRepo", "Time since last update: $timeDifference ms (${timeDifference / (60 * 60 * 1000)} hours)")
                timeDifference >= 12 * 60 * 60 * 1000 // 12 jam
            } ?: true // no data -> outDated
        }

        if (isOutdated) {
            try {
                val response = apiServices.getJournal()
                val journalList = response.listJournal?.map { journal ->
                    JournalHistoryEntity(
                        journalDate = formatDate(journal?.createdAt.toString()),
                        description = journal?.description,
                        photoUrl = journal?.photoUrl,
                        createdAt = System.currentTimeMillis()
                    )
                }
                withContext(Dispatchers.IO) {
                    journalDao.deleteAll()
                    Log.d("NourishRepo", "Old news deleted")
                    journalDao.insertIntoJournal(journalList!!)
                    Log.d("NourishRepo", "New journal inserted into database: ${journalList.size}")

                }
            } catch (e: Exception) {
                Log.d("NourishRepo", "getJournal: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        } else {
            val localData: LiveData<Result<List<JournalHistoryEntity>>> = journalDao.getAllJournal().map { journalList ->
                Log.d("NewsRepository", "Fetched news from DB: ${journalList.size}")
                Result.Success(journalList)
            }
            emitSource(localData)
        }
    }

    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    suspend fun saveSession(userModel: UserModel) = userPreference.saveSession(userModel)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: NourishRepository? = null
        fun getInstance(
            apiService: NourishApiServices,
            dataStoreToken: UserPreference,
            journalDao: JournalDao) = NourishRepository(apiService, dataStoreToken, journalDao)
    }
}