package com.example.weatherapp.ui.homeScreen

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import  com.example.weatherapp.model.Result
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.HomeFragmentBinding
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.adapters.CityListAdapter
import com.example.weatherapp.ui.CityClusterItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var googleMapInstance: GoogleMap
    private lateinit var clusterManager: ClusterManager<CityClusterItem>
    private lateinit var cityAdapter: CityListAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        cityAdapter = CityListAdapter()
    }

    private fun setupObservers() {
        homeViewModel.citiesList.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    googleMapInstance?.let {
                        it.clear()
                        clusterManager.clearItems()
                    }
                    var clusterItemList: List<CityClusterItem>
                    result.data?.results?.let { list ->
                        binding.cityRecycler.adapter = cityAdapter
                        cityAdapter.submitList(null)
                        cityAdapter.submitList(list)
                        clusterItemList = list.map {
                            CityClusterItem(it)
                        }
                        clusterManager.addItems(clusterItemList)
                        clusterManager.cluster()
                    }
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        Toast.makeText(context, "NO CITIES SAVED", Toast.LENGTH_SHORT).show()
                    }
                }

                Result.Status.LOADING -> {

                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        setUpViews(savedInstanceState)
        setupObservers()
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
        cityAdapter = CityListAdapter()

        binding.cityRecycler.layoutManager = LinearLayoutManager(context)
        binding.cityRecycler.adapter = cityAdapter
        binding.cityRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private val mapCallbacks = OnMapReadyCallback { googleMap ->
        googleMapInstance = googleMap
        googleMapInstance.setOnMapLongClickListener(onMapClickListener)

        clusterManager = ClusterManager(context, googleMap)
        clusterManager.setAnimation(false)
        val googleMapIconRenderer = DefaultClusterRenderer(context, googleMap, clusterManager)
        clusterManager.renderer = googleMapIconRenderer
        googleMapIconRenderer.minClusterSize = 2

        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
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
                googleMapInstance.addMarker(MarkerOptions().position(latitudeLongitude))
            }
        } catch (e: Exception) {
            Toast.makeText(context, "There is not city there", Toast.LENGTH_SHORT).show()
            Log.e("ERROR_TAG", e.message.toString())
        }
    }

}