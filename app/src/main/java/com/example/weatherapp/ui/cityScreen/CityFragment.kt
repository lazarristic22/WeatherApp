package com.example.weatherapp.ui.cityScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.databinding.CityFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityFragment : Fragment() {

    private lateinit var binding: CityFragmentBinding
    private val viewModel: CityViewModel by viewModels()
    private val args: CityFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.textViewCityName.text = args.city.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadWeather(args.city)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.weather.observe(viewLifecycleOwner, { weather ->
            weather?.let {
                // Here i could export this to string template
                binding.textViewTemperature.text = "${it.main.temp} C"
                binding.textViewHumidity.text = "${it.main.humidity} %"
                binding.textViewWind.text = "${it.wind.speed} KMPH"
                // I would need more time to parse JSON with numeric name
                // binding.textViewRain.text=it.rain.lastH.toString()+"mm"
            }
        })
    }
}