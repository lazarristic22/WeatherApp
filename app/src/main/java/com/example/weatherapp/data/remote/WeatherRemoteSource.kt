package com.example.weatherapp.data.remote

import com.example.weatherapp.model.Result
import com.example.weatherapp.model.dto.WeatherDto
import com.example.weatherapp.network.services.WeatherService
import com.example.weatherapp.utils.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRemoteSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchWeather(lat: Double, lon: Double): Result<WeatherDto> {
        val weatherService = retrofit.create(WeatherService::class.java);
        return getResponse(
            request = { weatherService.getWeather(lat, lon) },
            defaultErrorMessage = "Error fetching Weather list"
        )
    }


    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}