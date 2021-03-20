package com.example.weatherapp.ui.cityScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.CityFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityFragment : Fragment() {

    private lateinit var binding: CityFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}