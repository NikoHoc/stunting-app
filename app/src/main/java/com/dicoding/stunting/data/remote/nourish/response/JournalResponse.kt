package com.dicoding.stunting.data.remote.nourish.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class JournalResponse (
    @field:SerializedName("listStory")
    val listJournal: List<ListJournalItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class ListJournalItem(
    @field:SerializedName("journals_id")
    val journalsId: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photo_url")
    val photoUrl: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("longitude")
    val lon: Double? = null,

    @field:SerializedName("latitude")
    val lat: Double? = null
) : Parcelable