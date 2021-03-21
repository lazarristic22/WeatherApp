package com.example.weatherapp.ui.cityScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.model.City
import com.example.weatherapp.model.dto.WeatherDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val _weatherDto = MutableLiveData<WeatherDto>()
    val weather: LiveData<WeatherDto> get() = _weatherDto

    fun loadWeather(city: City) {
        viewModelScope.launch {
            weatherRepository.fetchWeather(city.latitude, city.longitude).onStart { }.collect {
                it.data?.let { weatherDto ->
                    _weatherDto.value = weatherDto
                }
            }
        }
    }
}