package org.ktc2.cokaen.wouldyouin.feat_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.PostItemBinding

class PostAdapter(
    private val events: List<EventResponse>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    inner class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventResponse) {
            // Post title
            binding.postTitle = event.title
            binding.postImageUrl = event.images[0]

            /*
            //행사 이미지
            Glide.with(binding.postImage.context)
                .load(event.host.profileImageUrl)
                .into(binding.postImage)
             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}