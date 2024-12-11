package com.dicoding.stunting.data.remote.nourish.response

import com.google.gson.annotations.SerializedName

data class AddJournalResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("journalId")
    val journalId: String? = null
)