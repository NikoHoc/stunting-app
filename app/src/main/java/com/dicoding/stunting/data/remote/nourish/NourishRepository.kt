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
import com.dicoding.stunting.data.remote.nourish.response.FoodRecResponse
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
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestFile
        )
        try {
            val successResponse = apiServices.uploadJournal(multipartBody, descriptionBody)
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
            Log.d("response journal", response.toString())
            val journalList = response.listJournal?.map { journal ->
                JournalHistoryEntity(
                    journalId = journal?.journalsId.toString(),
                    userId = journal?.userId,
                    journalDate = journal?.createdAt,
                    description = journal?.description,
                    photoUrl = journal?.photoUrl,
                    createdAt = System.currentTimeMillis()
                )
            } ?: emptyList()

            withContext(Dispatchers.IO) {
                if (journalList.isNotEmpty()) {
                    journalDao.deleteAll()
                    journalDao.insertIntoJournal(journalList)
                    Log.d("NourishRepo", "Journal updated in database: ${journalList.size}")
                }
            }
            emit(Result.Success(journalList))
        } catch (e: Exception) {
            Log.d("NourishRepo", "Error fetching journal: ${e.message}")
            val localData = journalDao.getJournalsByUserId(userId)
            emitSource(localData.map { Result.Success(it) })
        }
    }

    fun getPredictionHistory(userId: String): LiveData<Result<List<PredictionHistoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getPredictionHistory()
            Log.d("Response Get Predict", response.toString())
            val predictionList = response.data?.map { prediction ->
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
            } ?: emptyList()

            withContext(Dispatchers.IO) {
                if (predictionList.isNotEmpty()) {
                    predictionDao.deleteAll()
                    predictionDao.insertIntoPredictions(predictionList)
                    Log.d("Prediction List", "Predictions updated in database: ${predictionList.size}")
                }
            }
            emit(Result.Success(predictionList))
        } catch (e: Exception) {
            Log.d("Prediction list", "Error fetching predictions: ${e.message}")
            val localData = predictionDao.getPredictionsByUserId(userId)
            emitSource(localData.map { Result.Success(it) })
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

    fun isPredictionExists(age: Int, height: Float, gender: String, result: String): LiveData<Boolean> {
        return predictionDao.isPredictionExists(age, height, gender, result)
    }

    fun getFoodRecommendation(classification: Int) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getFoodRecommendation(classification)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FoodRecResponse::class.java)
            emit(Result.Error(errorResponse.toString()))
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