package com.dicoding.stunting.data.remote

import androidx.lifecycle.liveData
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.pref.UserPreference
import com.dicoding.stunting.data.remote.retrofit.ApiServices
import com.google.gson.Gson
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.response.LoginResponse
import com.dicoding.stunting.data.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class DataRepository private constructor(
    private val apiServices: ApiServices,
    private val userPreference: UserPreference
) {

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
        private var instance: DataRepository? = null
        fun getInstance(apiService: ApiServices, dataStoreToken: UserPreference) = DataRepository(apiService, dataStoreToken)
    }
}