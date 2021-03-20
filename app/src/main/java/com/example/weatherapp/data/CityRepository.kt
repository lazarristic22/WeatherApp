package com.example.weatherapp.data

import  com.example.weatherapp.model.Result
import  com.example.weatherapp.model.Error
import com.example.weatherapp.data.local.CityDao
import com.example.weatherapp.model.CitiesResponse
import com.example.weatherapp.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao
) {

    suspend fun fetchCities(): Flow<Result<CitiesResponse>?> {
        return flow {
            emit(fetchCitiesCached())
            //currently we have no fetching from Network,but it could be easily integrated here
        }.flowOn(Dispatchers.IO)
    }


    private fun fetchCitiesCached(): Result<CitiesResponse>? =
        cityDao.getAll()?.let {
            if (it.isNotEmpty()) {
                Result.success(CitiesResponse(it))
            } else {
                Result.error("No Cities Saved", Error(404))
            }
        }

    suspend fun insertCity(city: City) {
        cityDao.insert(city)
    }
}