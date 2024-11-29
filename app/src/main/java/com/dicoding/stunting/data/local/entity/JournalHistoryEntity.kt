package com.dicoding.stunting.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
class JournalHistoryEntity (
    @ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "journal_date")
    val journalDate: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)