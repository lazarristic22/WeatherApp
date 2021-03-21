package com.example.weatherapp.data

import com.example.weatherapp.data.local.CityDao
import com.example.weatherapp.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao
) {

     fun getAllCitiesFlow(): Flow<List<City>> =
        cityDao.getAll().flowOn(Dispatchers.IO).conflate()

    suspend fun insertCity(city: City) {
        cityDao.insert(city)
    }

    suspend fun deleteCity(city: City) {
        cityDao.delete(city)
    }
}