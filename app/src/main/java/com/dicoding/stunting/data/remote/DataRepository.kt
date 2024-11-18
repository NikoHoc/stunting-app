package com.dicoding.stunting.data.remote

import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.pref.UserPreference
import com.dicoding.stunting.data.remote.retrofit.ApiServices
import kotlinx.coroutines.flow.Flow

class DataRepository private constructor(
    private val apiServices: ApiServices,
    private val userPreference: UserPreference
) {


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