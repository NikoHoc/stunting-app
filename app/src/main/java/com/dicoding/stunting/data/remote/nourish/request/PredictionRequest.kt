package com.dicoding.stunting.data.remote.nourish.request

data class PredictionRequest(
    val age: Int,
    val gender: String,
    val height: Float,
    val result: String,
    val description: String
)