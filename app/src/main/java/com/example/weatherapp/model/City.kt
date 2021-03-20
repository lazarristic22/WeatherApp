package com.example.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double
)