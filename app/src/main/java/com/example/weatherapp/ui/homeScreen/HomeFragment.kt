package com.example.weatherapp.ui.homeScreen

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.databinding.HomeFragmentBinding
import com.example.weatherapp.model.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var googleMapInstance: GoogleMap

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        setUpViews(savedInstanceState)
        return binding.root
    }

    private fun setUpViews(savedInstanceState: Bundle?) {
        binding.googleMapView.onCreate(savedInstanceState)
        binding.googleMapView.onResume()
        try {
            MapsInitializer.initialize(context)
        } catch (e: Exception) {
            Log.e("MAP_EXCEPTION", e.message.toString())
        }
        binding.googleMapView.getMapAsync(mapCallbacks)
    }

    private val mapCallbacks = OnMapReadyCallback { googleMap ->
        googleMapInstance = googleMap
        googleMapInstance.setOnMapLongClickListener(onMapClickListener)

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private val onMapClickListener = GoogleMap.OnMapLongClickListener { latitudeLongitude ->
        val geoCoder = Geocoder(context)
        try {
            val adressList =
                geoCoder.getFromLocation(
                    latitudeLongitude.latitude,
                    latitudeLongitude.longitude,
                    1
                )
            adressList.first()?.let { address ->
                val cityName = address.subAdminArea
                val city =
                    City(0, cityName, latitudeLongitude.latitude, latitudeLongitude.longitude)
                homeViewModel.insertCity(city)
            }
        } catch (e: Exception) {
            Log.e("ERROR_TAG", e.message.toString())
        }

        googleMapInstance.addMarker(MarkerOptions().position(latitudeLongitude))

    }
}