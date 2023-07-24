package com.asas.phapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PHDao {
    @Query("SELECT * FROM PH")
    fun getAll(): Flow<List<Device>>

    @Insert
    suspend fun insert(entity: Device)
}

