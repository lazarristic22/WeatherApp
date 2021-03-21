package com.example.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CityItemBinding
import com.example.weatherapp.model.City

class CityListAdapter :
    ListAdapter<City, CityListAdapter.CityViewHolder>(CityAdapterDiffUtilItemCallback) {

    var onClickListener: ((City) -> Unit)? = null
    var onSwipeDelete: ((City) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding =
            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    fun deleteItem(position: Int) {
        onSwipeDelete?.invoke(currentList[position])
    }

    inner class CityViewHolder(private val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var boundCity: City

        init {
            setupOnClickListener()
        }

        private fun setupOnClickListener() {
            itemView.setOnClickListener {
                onClickListener?.invoke(boundCity)
            }
        }

        fun bind(city: City) {
            boundCity = city
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