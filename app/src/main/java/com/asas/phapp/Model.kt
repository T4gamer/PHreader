package com.asas.phapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Readings")
data class Reading(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "reading") val reading: Double,
    @ColumnInfo(name = "temperature") val temp: Double,
    @ColumnInfo(name = "place") val place: String
)
