package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.SearchEventData
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.SelectedEventItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class SelectedEventsAdapter(
    private var eventList: List<SearchEventData>,
    private val viewModel: CreateCurationViewModel
) : RecyclerView.Adapter<SelectedEventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            SelectedEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    fun setData(events: List<SearchEventData>) {
        eventList = events.toMutableList()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)

        holder.binding.deleteButton.setOnClickListener {
            // 삭제할 아이템의 position 전달 (삭제는 Activity에서 처리)
            viewModel.deleteEvent(position)
            notifyItemRemoved(position) // UI에서 해당 항목을 제거
        }
    }

    override fun getItemCount(): Int = eventList.size

    inner class EventViewHolder(val binding: SelectedEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: SearchEventData) {
            binding.eventTitle.text = event.eventName
            binding.hostName.text = event.hostName
            binding.imageUrl = event.imageUrl
        }
    }
}