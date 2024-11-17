package com.example.feat_likes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feat_likes.viewModel.CuratorLikesViewModel
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.LikedOrganizerItemBinding

class LikedCuratorAdapter :
    ListAdapter<LikeResponse, LikedCuratorAdapter.MemberViewHolder>(LikeResponseDiffCallback()) {

    var onHeartClick: ((LikeResponse) -> Unit)? = null
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
            binding.apply {
                organizerName.text = member.nickname
                organizerInfo.text = member.intro
                imageUrl = member.profileImageUrl
                hashtag.adapter = HashtagAdapter(member.hashtags)

                hashtag.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

                // 하트 버튼 클릭
                heartButton.setOnClickListener {
                    onHeartClick?.invoke(member)  // onHeartClick 사용
                }

                // 프로필 클릭
                memberProfile.setOnClickListener {
                    onItemClick?.invoke(member)
                }

                // root 클릭도 추가하면 좋을 수 있음
                root.setOnClickListener {
                    onItemClick?.invoke(member)
                }
            }
            binding.executePendingBindings()
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
