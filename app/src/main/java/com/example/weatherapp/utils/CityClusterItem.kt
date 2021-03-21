package com.example.weatherapp.utils

import com.example.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/*
This class is used for clustering markers on the map so that map isn't overloaded by markers
 */
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