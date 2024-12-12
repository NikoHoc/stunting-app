package com.dicoding.stunting.data.remote.nourish.response

import com.google.gson.annotations.SerializedName

data class FoodRecResponse(

	@field:SerializedName("classification")
	val classification: String? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class RecommendationsItem(

	@field:SerializedName("vitamin_a")
	val vitaminA: Int? = null,

	@field:SerializedName("fats")
	val fats: Float? = null,

	@field:SerializedName("vitamin_c")
	val vitaminC: Float? = null,

	@field:SerializedName("vitamin_b2")
	val vitaminB2: Float? = null,

	@field:SerializedName("serving_size")
	val servingSize: Float? = null,

	@field:SerializedName("protein")
	val protein: Float? = null,

	@field:SerializedName("vitamin_d")
	val vitaminD: Float? = null,

	@field:SerializedName("vitamin_b1")
	val vitaminB1: Float? = null,

	@field:SerializedName("vitamin_e")
	val vitaminE: Float? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("food")
	val food: String? = null
)
