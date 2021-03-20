package com.example.weatherapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CityItemBinding
import com.example.weatherapp.model.City

class CityListAdapter :
    ListAdapter<City, CityListAdapter.CityViewHolder>(CityAdapterDiffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.i("BOBAN", "CREATING VIEW HOLDER")
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        Log.i("BOBAN", "IN onBindViewHolder at position ${position}")
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CityViewHolder(private val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            Log.i("BOBAN", "I AM BINDING ${city.name}")
            binding.textViewCityName.text = city.name
        }
    }

    companion object CityAdapterDiffUtilItemCallback : DiffUtil.ItemCallback<City>() {

        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }
    }
}