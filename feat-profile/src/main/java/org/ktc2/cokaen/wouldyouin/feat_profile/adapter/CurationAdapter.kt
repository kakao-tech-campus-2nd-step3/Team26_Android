package org.ktc2.cokaen.wouldyouin.feat_profile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.CuratorPostItemBinding

class CurationAdapter(
    private val curations: List<CurationResponse>
) : RecyclerView.Adapter<CurationAdapter.PostViewHolder>() {

    private var onItemClickListener: ((CurationResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (CurationResponse) -> Unit) {
        onItemClickListener = listener
    }

    inner class PostViewHolder(private val binding: CuratorPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curation: CurationResponse) {
            binding.postTitle = curation.title
            binding.postImageUrl = curation.thumbnailUrl
            Log.d("ThumbnailUrl", "${curation.thumbnailUrl}")

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(curation)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CuratorPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(curations[position])
    }

    override fun getItemCount(): Int = curations.count()
}