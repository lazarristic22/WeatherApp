package com.example.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @NonNull
    @PrimaryKey
    val id: Int,
    val title: String?
)