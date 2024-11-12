package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.SearchEventData
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.SelectedEventItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class SelectedEventsAdapter(
    private val viewModel: CreateCurationViewModel
) : RecyclerView.Adapter<SelectedEventsAdapter.EventViewHolder>() {

    private var eventList: List<SearchEventData> = emptyList() // 초기 데이터는 빈 리스트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            SelectedEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    // 데이터를 갱신하는 메소드
    fun setData(events: List<SearchEventData>) {
        eventList = events
        notifyDataSetChanged() // 데이터가 변경될 때마다 UI 갱신
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)

        // 삭제 버튼 클릭 시, position 전달하여 삭제 처리
        holder.binding.deleteButton.setOnClickListener {
            viewModel.deleteEvent(position) // 삭제는 ViewModel에서 처리
            notifyItemRemoved(position) // UI에서 항목 제거
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
