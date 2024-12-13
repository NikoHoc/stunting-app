package com.dicoding.stunting.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "prediction")
class PredictionHistoryEntity(
    @ColumnInfo(name = "prediction_id")
    @field:PrimaryKey
    var predictionId: String,

    @ColumnInfo(name = "age")
    val age: Int? = null,

    @ColumnInfo(name = "gender")
    val gender: String? = null,

    @ColumnInfo(name = "height")
    val height: Float? = null,

    @ColumnInfo(name = "result")
    val result: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "user_id")
    val userId: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: String?
)

