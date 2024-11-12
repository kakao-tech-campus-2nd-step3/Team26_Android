package org.ktc2.cokaen.wouldyouin.feat_profile.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.HashtagItemBinding

class HashtagAdapter(private val hashtags: List<String>) :
    RecyclerView.Adapter<HashtagAdapter.HashtagViewHolder>()  {
    inner class HashtagViewHolder(private val binding: HashtagItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hashtag: String) {
            binding.hashtag.text = hashtag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagViewHolder {
        val binding = HashtagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HashtagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HashtagViewHolder, position: Int) {
        holder.bind(hashtags[position])
    }

    override fun getItemCount(): Int = hashtags.size
}