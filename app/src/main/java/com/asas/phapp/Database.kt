package com.asas.phapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reading::class], version =1)
abstract class PHDatabase : RoomDatabase() {
    abstract fun PHDao(): PHDao
}
