package com.asas.phapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PH")
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "token") val token: String,
    @ColumnInfo(name = "place") val place: String
)
