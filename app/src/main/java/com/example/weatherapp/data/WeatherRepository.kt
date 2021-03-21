package com.example.weatherapp.data

import com.example.weatherapp.data.remote.WeatherRemoteSource
import com.example.weatherapp.model.Result
import com.example.weatherapp.model.dto.WeatherDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherRemoteSource: WeatherRemoteSource) {

    suspend fun fetchWeather(lat: Double, lon: Double): Flow<Result<WeatherDto>> {
        return flow {
            emit(Result.loading())
            emit(weatherRemoteSource.fetchWeather(lat, lon))
        }.flowOn(Dispatchers.IO)
    }
}