package com.example.weatherapp.ui.homeScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import  com.example.weatherapp.model.Result
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CityRepository
import com.example.weatherapp.model.CitiesResponse
import com.example.weatherapp.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val cityRepository: CityRepository) : ViewModel() {

    private val _citiesList = MutableLiveData<Result<CitiesResponse>>()
    val citiesList = _citiesList

    init {
       fetchCities()
    }

    private fun fetchCities() {
        viewModelScope.launch {
            cityRepository.fetchCities().collect {
                _citiesList.value = it
            }
        }
    }


    fun insertCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.insertCity(city)
        }
    }
}