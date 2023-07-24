package com.asas.phapp

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Database(entities = [Device::class], version = 1)
abstract class PHDatabase : RoomDatabase() {
    abstract fun PHDao(): PHDao
}

@Module
@InstallIn(SingletonComponent::class)
object MyModule {
    @Provides
    fun provideDatabase(application: Application): PHDatabase {
        return Room.databaseBuilder(
            application, PHDatabase::class.java, "PHDatabase"
        ).build()
    }

    @Provides
    fun provideMyDao(database: PHDatabase): PHDao {
        return database.PHDao()
    }
}
