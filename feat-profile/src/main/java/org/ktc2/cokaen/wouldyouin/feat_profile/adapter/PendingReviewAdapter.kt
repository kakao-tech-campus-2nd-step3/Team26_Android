package org.ktc2.cokaen.wouldyouin.feat_profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ItemPendingReviewBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.PendingReview

class PendingReviewAdapter(
    private val onItemClick: (Long) -> Unit
) : ListAdapter<PendingReview, PendingReviewAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPendingReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemPendingReviewBinding,
        private val onItemClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: PendingReview) {
            binding.apply {
                // review 객체를 직접 바인딩 변수에 할당
                this.review = review

                // 클릭 리스너 설정
                root.setOnClickListener {
                    onItemClick(review.eventId)
                }

                // 즉시 바인딩 실행
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PendingReview>() {
            override fun areItemsTheSame(oldItem: PendingReview, newItem: PendingReview): Boolean {
                return oldItem.eventId == newItem.eventId
            }

            override fun areContentsTheSame(oldItem: PendingReview, newItem: PendingReview): Boolean {
                return oldItem == newItem
            }
        }
    }
}