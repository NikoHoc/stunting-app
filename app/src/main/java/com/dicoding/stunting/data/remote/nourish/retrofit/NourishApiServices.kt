package com.dicoding.stunting.data.remote.nourish.retrofit

import com.dicoding.stunting.data.remote.nourish.request.LoginRequest
import com.dicoding.stunting.data.remote.nourish.request.RegisterRequest
import com.dicoding.stunting.data.remote.nourish.response.AddJournalResponse
import com.dicoding.stunting.data.remote.nourish.response.JournalResponse
import com.dicoding.stunting.data.remote.nourish.response.LoginResponse
import com.dicoding.stunting.data.remote.nourish.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NourishApiServices {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    /* Nourish */
//    @POST("auth/register")
//    suspend fun register(
//        @Body request: RegisterRequest
//    ): RegisterResponse

//    @POST("auth/login")
//    suspend fun login(
//        @Body request: LoginRequest
//    ): LoginResponse


    @GET("stories")
    suspend fun getJournal(): JournalResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : AddJournalResponse
}