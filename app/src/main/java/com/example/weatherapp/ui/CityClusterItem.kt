package com.example.weatherapp.ui

import com.example.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class CityClusterItem(val city: City) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(city.latitude, city.longitude)
    }

    override fun getTitle(): String? {
        return city.name
    }

    override fun getSnippet(): String? {
        return city.name
    }
}