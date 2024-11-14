package com.example.feat_likes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feat_likes.viewModel.CuratorLikesViewModel
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.LikedOrganizerItemBinding

class LikedCuratorAdapter :
    ListAdapter<LikeResponse, LikedCuratorAdapter.MemberViewHolder>(LikeResponseDiffCallback()) {

    var onItemClick: ((LikeResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = LikedOrganizerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MemberViewHolder(
        private val binding: LikedOrganizerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(member: LikeResponse) {
            binding.organizerName.text = member.nickname
            val hashtagAdapter = HashtagAdapter(member.hashtags)
            binding.hashtag.adapter = hashtagAdapter

            binding.heartButton.setOnClickListener {
                onItemClick?.invoke(member)
            }
        }
    }
}

// DiffUtil 구현
class LikeResponseDiffCallback : DiffUtil.ItemCallback<LikeResponse>() {
    override fun areItemsTheSame(oldItem: LikeResponse, newItem: LikeResponse): Boolean {
        return oldItem.memberId == newItem.memberId
    }

    override fun areContentsTheSame(oldItem: LikeResponse, newItem: LikeResponse): Boolean {
        return oldItem == newItem
    }
}
