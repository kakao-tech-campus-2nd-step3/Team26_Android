package org.ktc2.cokaen.wouldyouin.feat_event.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.AdvertisementResponse
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.AdItemBinding

class AdAdapter (private var adList: List<AdvertisementResponse>) : RecyclerView.Adapter<AdAdapter.AdViewHolder>() {
    inner class AdViewHolder(private val binding: AdItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(advertisement: AdvertisementResponse) {
            binding.imageUrl = advertisement.imageUrl
            Log.d("AdAdapter", "Binding imageUrl: ${advertisement.imageUrl}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = AdItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(adList[position])
    }

    override fun getItemCount(): Int = adList.size

    fun updateAdList(newAdList: List<AdvertisementResponse>) {
        adList = newAdList
        Log.d("AdAdapter", "Updated ad list with size: ${adList.size}")
        notifyDataSetChanged()
    }
}