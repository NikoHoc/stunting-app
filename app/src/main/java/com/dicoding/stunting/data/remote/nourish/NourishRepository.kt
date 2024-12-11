package com.dicoding.stunting.data.remote.nourish

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity
import com.dicoding.stunting.data.local.room.JournalDao
import com.dicoding.stunting.data.local.pref.UserModel
import com.dicoding.stunting.data.local.pref.UserPreference
import com.dicoding.stunting.data.local.room.PredictionDao
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.data.remote.nourish.response.AddJournalResponse
import com.dicoding.stunting.data.remote.nourish.retrofit.NourishApiServices
import com.google.gson.Gson
import com.dicoding.stunting.data.remote.nourish.response.LoginResponse
import com.dicoding.stunting.data.remote.nourish.response.RegisterResponse
import com.dicoding.stunting.data.remote.nourish.request.LoginRequest
import com.dicoding.stunting.data.remote.nourish.request.PredictionRequest
import com.dicoding.stunting.data.remote.nourish.request.RegisterRequest
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
    private val journalDao: JournalDao,
    private val predictionDao: PredictionDao

) {
    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.register(RegisterRequest(name, email, password))
//            val response = apiServices.register(name, email, password)
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
            Log.d("Login Data Repository", "$email $password")
//            val response = apiServices.login(email, password)
            val response = apiServices.login(LoginRequest(email, password))
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            Log.d("Login Error", errorResponse.message.toString())
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

    fun uploadJournal(imageFile: File, description: String) = liveData {
        emit(Result.Loading)
//        val requestBody = description.toRequestBody("text/plain".toMediaType())
//        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//        val multipartBody = MultipartBody.Part.createFormData(
//            "photo",
//            imageFile.name,
//            requestImageFile
//        )
        // Create RequestBody for the description
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        // Create MultipartBody.Part for the image file
        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestFile
        )
        try {
            val successResponse = apiServices.uploadJournal(multipartBody, descriptionBody)
//            val successResponse = apiServices.uploadJournal(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddJournalResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }

    }

    fun getJournal(userId: String): LiveData<Result<List<JournalHistoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getJournal()
            Log.d("respose journal", response.toString())

            val journalList = response.listJournal?.filter { journal ->
                journal?.userId.toString() == userId
            }?.map { journal ->
                JournalHistoryEntity(
                    journalId = journal?.journalsId.toString(),
                    userId = journal?.userId,
                    journalDate = journal?.createdAt,
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
                emit(Result.Success(journalList))
            }
        } catch (e: Exception) {
            Log.d("NourishRepo", "getJournal: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPredictionHistory(userId: String): LiveData<Result<List<PredictionHistoryEntity>>>  = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getPredictionHistory()
            Log.d("Response Get Predict", response.toString())
            val predictionList = response.data?.filter { prediction ->
                prediction?.userId.toString() == userId
            }?.map { prediction ->
                PredictionHistoryEntity(
                    predictionId = prediction?.predictionId.toString(),
                    age = prediction?.age,
                    gender = prediction?.gender.toString(),
                    height = prediction?.height?.toFloat(),
                    result = prediction?.result.toString(),
                    description = prediction?.description.toString(),
                    userId = prediction?.userId.toString(),
                    createdAt = prediction?.createdAt
                )
            }
            withContext(Dispatchers.IO) {
                predictionDao.deleteAll()
                Log.d("Prediction List", "Old Prediction deleted")
                predictionDao.insertIntoPredictions(predictionList!!)
                Log.d("Prediction List", "New Prediction inserted into database: ${predictionList.size}")
                emit(Result.Success(predictionList))
            }
        } catch (e: Exception) {
            Log.d("Prediction list", "getPrediction: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadPredict(age: Int, gender: String, height: Float, result: String, description: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.uploadPrediction(PredictionRequest(age, gender, height, result, description))
//            val response = apiServices.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: NourishRepository? = null
        fun getInstance(
            apiService: NourishApiServices,
            dataStoreToken: UserPreference,
            journalDao: JournalDao,
            predictionDao: PredictionDao) = NourishRepository(apiService, dataStoreToken, journalDao, predictionDao)
    }
}