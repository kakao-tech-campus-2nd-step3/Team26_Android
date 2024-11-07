package com.example.feat_likes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.MemberResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.LikedOrganizerItemBinding

class LikedMemberAdapter : RecyclerView.Adapter<LikedMemberAdapter.MemberViewHolder>() {

    private var members = listOf<MemberResponse>()
    var onItemClick: ((MemberResponse) -> Unit)? = null

    fun submitList(newList: List<MemberResponse>) {
        members = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = LikedOrganizerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount() = members.size

    inner class MemberViewHolder(
        private val binding: LikedOrganizerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(members[position])
                }
            }
        }

        fun bind(member: MemberResponse) {
//            binding.apply {
//                this.member = member  // 데이터 바인딩에 멤버 객체 할당
//
//                // 멤버 타입별 특수 처리
//                when (member) {
//                    is HostMemberResponse -> {
//                        organizerName.text = member.nickname
//                        organizerInfo.text = member.intro
//                        hashtag.visibility = View.VISIBLE
//
//                        // 해시태그 처리
//                        val hashtagAdapter = HashtagAdapter()
//                        hashtag.adapter = hashtagAdapter
//                        hashtag.layoutManager = LinearLayoutManager(
//                            itemView.context,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                        )
//                        hashtagAdapter.submitList(member.hashtag)
//                    }
//
//                    is CuratorMemberResponse -> {
//                        organizerName.text = member.nickname
//                        organizerInfo.text = member.intro
//                        hashtag.visibility = View.GONE
//                    }
//
//                    is NormalMemberResponse -> {
//                        organizerName.text = member.nickname
//                        organizerInfo.visibility = View.GONE
//                        hashtag.visibility = View.GONE
//                    }
//                }
//
//                executePendingBindings()
//            }
        }
    }
}