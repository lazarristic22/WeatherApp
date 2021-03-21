package com.example.weatherapp.model.dto

import com.google.gson.annotations.SerializedName

data class RainDto(
    @SerializedName("1h")
    val lastH: Int
)
