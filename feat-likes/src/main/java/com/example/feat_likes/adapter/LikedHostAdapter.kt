package com.example.feat_likes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feat_likes.viewModel.CuratorLikesViewModel
import com.example.feat_likes.viewModel.HostLikesViewModel
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.LikedOrganizerItemBinding

class LikedHostAdapter :
    ListAdapter<LikeResponse, LikedHostAdapter.MemberViewHolder>(LikeResponseDiffCallback()) {

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

        var onHeartClick: ((LikeResponse) -> Unit)? = null
        var onItemClick: ((LikeResponse) -> Unit)? = null


        fun bind(member: LikeResponse) {
            binding.apply {
                organizerName.text = member.nickname
                hashtag.adapter = HashtagAdapter(member.hashtags)

                heartButton.setOnClickListener {
                    onHeartClick?.invoke(member)
                }

                memberProfile.setOnClickListener {
                    onItemClick?.invoke(member)
                }

                root.setOnClickListener {
                    onItemClick?.invoke(member)
                }
            }
        }
    }
}