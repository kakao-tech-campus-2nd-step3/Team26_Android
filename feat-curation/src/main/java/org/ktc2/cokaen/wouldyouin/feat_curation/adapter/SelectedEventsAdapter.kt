package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.SearchEventData
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.SelectedEventItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class SelectedEventsAdapter(
    private val viewModel: CreateCurationViewModel
) : ListAdapter<SearchEventData, SelectedEventsAdapter.EventViewHolder>(EventDiffCallback) {

    // DiffUtil.ItemCallback 구현
    companion object {
        private object EventDiffCallback : DiffUtil.ItemCallback<SearchEventData>() {
            override fun areItemsTheSame(oldItem: SearchEventData, newItem: SearchEventData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: SearchEventData, newItem: SearchEventData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            SelectedEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        // 삭제 버튼 클릭 처리
        holder.binding.deleteButton.setOnClickListener {
            viewModel.deleteEvent(position)
            // notifyItemRemoved는 필요 없음 - ViewModel에서 새 리스트를 제공할 것이기 때문
        }
    }

    inner class EventViewHolder(val binding: SelectedEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: SearchEventData) {
            binding.apply {
                eventTitle.text = event.eventName
                hostName.text = event.hostName
                imageUrl = event.imageUrl
                executePendingBindings()
            }
        }
    }
}
