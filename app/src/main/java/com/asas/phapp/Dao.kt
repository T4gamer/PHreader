package com.asas.phapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PHDao {
    @Query("SELECT * FROM Readings")
    fun getAll(): Flow<List<Reading>>

    @Query("SELECT * FROM Readings WHERE place = :name")
    fun getByName(name: String): List<Reading>

    @Insert
    suspend fun insert(entity: Reading)

    @Delete
    suspend fun delete(entity: Reading)
}

