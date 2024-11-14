package org.ktc2.cokaen.wouldyouin.feat_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.PostItemBinding

class CurationAdapter(
    private val curations: List<CurationResponse>
) : RecyclerView.Adapter<CurationAdapter.PostViewHolder>() {

    private var onItemClickListener: ((CurationResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (CurationResponse) -> Unit) {
        onItemClickListener = listener
    }

    inner class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curation: CurationResponse) {
            // Post title
            binding.postTitle = curation.title
            binding.postImageUrl = curation.thumbnailUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(curations[position])
    }

    override fun getItemCount(): Int = curations.count()
}