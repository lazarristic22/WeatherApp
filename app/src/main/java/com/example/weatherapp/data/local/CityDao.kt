package com.example.weatherapp.data.local

import androidx.room.*
import com.example.weatherapp.model.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city ")
    fun getAll(): Flow<List<City>>

    @Delete
    suspend fun delete(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City): Long

}