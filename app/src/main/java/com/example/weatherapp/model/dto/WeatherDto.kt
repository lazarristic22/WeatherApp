package com.example.weatherapp.model.dto

data class WeatherDto(
    val base: String,
    val main: MainDto,
    val rain: RainDto,
    val wind: WindDto,
)
