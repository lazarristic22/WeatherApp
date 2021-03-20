package com.example.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.model.City

@Database(entities = [City::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    //add MyDaoClasses Here
//    abstract fun movieDao(): MovieDao
}