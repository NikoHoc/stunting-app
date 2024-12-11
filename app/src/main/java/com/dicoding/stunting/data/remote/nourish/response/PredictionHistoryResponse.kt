package com.dicoding.stunting.data.remote.nourish.response

import com.google.gson.annotations.SerializedName

data class PredictionHistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("prediction_id")
	val predictionId: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("height")
	val height: Float? = null
)
