package com.example.weatherapp.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CityRepository
import com.example.weatherapp.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val cityRepository: CityRepository) : ViewModel() {

    val citiesList = cityRepository.getAllCitiesFlow().asLiveData()

    fun insertCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.insertCity(city)
        }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.deleteCity(city)
        }
    }
}