package org.ktc2.cokaen.wouldyouin.feat_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.HostHashtagItemBinding

class HashtagAdapter(private val hashtags: List<String>) :
    RecyclerView.Adapter<HashtagAdapter.HashtagViewHolder>()  {
    inner class HashtagViewHolder(private val binding: HostHashtagItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hashtag: String) {
            binding.hashtag.text = hashtag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagViewHolder {
        val binding = HostHashtagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HashtagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HashtagViewHolder, position: Int) {
        holder.bind(hashtags[position])
    }

    override fun getItemCount(): Int = hashtags.size
}