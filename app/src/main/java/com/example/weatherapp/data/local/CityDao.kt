package com.example.weatherapp.data.local

import androidx.room.*
import com.example.weatherapp.model.City

@Dao
interface CityDao {

    @Query("SELECT * FROM city ")
    fun getAll(): List<City>?

    @Delete
    fun delete(movie: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City): Long

}