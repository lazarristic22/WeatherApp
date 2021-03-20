package com.example.weatherapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.local.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {

        Log.i("BOBAN","BUILDING DATABASE")

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()

    }

    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): CityDao {
        return appDatabase.cityDao()
    }
}