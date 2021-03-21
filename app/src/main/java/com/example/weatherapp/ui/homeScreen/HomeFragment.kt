package com.example.weatherapp.ui.homeScreen

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.HomeFragmentBinding
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.adapters.CityListAdapter
import com.example.weatherapp.utils.CityClusterItem
import com.example.weatherapp.utils.SwipeToDelete
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
        cityAdapter.onClickListener = { city ->
            val action = HomeFragmentDirections.actionHomeFragmentToCityFragment(city)
            findNavController().navigate(action)
        }
        cityAdapter.onSwipeDelete = { city ->
            homeViewModel.deleteCity(city)
        }
    }


    private fun setupObservers() {
        homeViewModel.citiesList.observe(viewLifecycleOwner, { list ->
            googleMapInstance.clear()
            clusterManager.clearItems()
            cityAdapter.submitList(list)
            val clusterItemList: List<CityClusterItem> = list.map {
                CityClusterItem(it)
            }
            clusterManager.addItems(clusterItemList)
            clusterManager.cluster()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        setupMap(savedInstanceState)
        setUpViews()
        return binding.root
    }

    private fun setUpViews() {
        setupRecycler()
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        with(binding.googleMapView) {
            onCreate(savedInstanceState)
            onResume()
            try {
                MapsInitializer.initialize(context)
            } catch (e: Exception) {
                Log.e("MAP_EXCEPTION", e.message.toString())
            }
            getMapAsync(mapCallbacks)
        }
    }

    private fun setupRecycler() {
        with(binding.cityRecycler) {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            binding.cityRecycler.layoutManager = linearLayoutManager
            binding.cityRecycler.adapter = cityAdapter
            binding.cityRecycler.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            val itemTouchHelper = ItemTouchHelper(SwipeToDelete(cityAdapter))
            itemTouchHelper.attachToRecyclerView(this)
        }

    }

    /**
     *  [mapCallbacks] is used for map functionality like [OnMapReadyCallback] which sets
     *  googleMapInstance
     */

    private val mapCallbacks = OnMapReadyCallback { googleMap ->
        googleMapInstance = googleMap
        googleMapInstance.clear()
        googleMapInstance.setOnMapLongClickListener(onMapClickListener)

        clusterManager = ClusterManager(context, googleMap)
        clusterManager.setAnimation(false)
        val googleMapIconRenderer = DefaultClusterRenderer(context, googleMap, clusterManager)
        clusterManager.renderer = googleMapIconRenderer
        googleMapIconRenderer.minClusterSize = 2

        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
        setupObservers()
    }

    /**
     *  Here I call only the first address from the GeoCoder even though there could be more
     */
    private val onMapClickListener = GoogleMap.OnMapLongClickListener { latitudeLongitude ->
        val geoCoder = Geocoder(context)
        try {
            val addressList =
                geoCoder.getFromLocation(
                    latitudeLongitude.latitude,
                    latitudeLongitude.longitude,
                    1
                )
            addressList.first()?.let { address ->
                val cityName = address.subAdminArea
                val city =
                    City(0, cityName, latitudeLongitude.latitude, latitudeLongitude.longitude)
                cityName?.let {
                    homeViewModel.insertCity(city)
                    googleMapInstance.addMarker(MarkerOptions().position(latitudeLongitude))
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "There is no city there", Toast.LENGTH_SHORT).show()
            Log.e("ERROR_TAG", e.message.toString())
        }
    }

}