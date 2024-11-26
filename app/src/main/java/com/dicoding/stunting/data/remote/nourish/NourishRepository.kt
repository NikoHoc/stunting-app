package com.dicoding.stunting.data.remote.nourish

import androidx.lifecycle.liveData
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.pref.UserPreference
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.response.AddJournalResponse
import com.dicoding.stunting.data.remote.nourish.response.JournalResponse
import com.dicoding.stunting.data.remote.nourish.retrofit.ApiServices
import com.google.gson.Gson
import com.dicoding.stunting.data.remote.nourish.response.LoginResponse
import com.dicoding.stunting.data.remote.nourish.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class NourishRepository private constructor(
    private val apiServices: ApiServices,
    private val userPreference: UserPreference
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

    fun getJournal() = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getJournal()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, JournalResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
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
        fun getInstance(apiService: ApiServices, dataStoreToken: UserPreference) = NourishRepository(apiService, dataStoreToken)
    }
}