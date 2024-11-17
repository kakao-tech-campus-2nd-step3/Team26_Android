package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.data.model.CurationCardResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationEventResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemCurationEventBinding

class DetailCurationEventAdapter(
    private val onEventClick: (CurationEventResponse) -> Unit
) : ListAdapter<CurationEventResponse, DetailCurationEventAdapter.CurationEventViewHolder>(

    object : DiffUtil.ItemCallback<CurationEventResponse>() {

        override fun areItemsTheSame(oldItem: CurationEventResponse, newItem: CurationEventResponse): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: CurationEventResponse, newItem: CurationEventResponse): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationEventViewHolder {
        val binding = ItemCurationEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurationEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurationEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurationEventViewHolder(private val binding: ItemCurationEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: CurationEventResponse) {
            binding.apply {
                eventResponse = event  // 데이터 바인딩 변수에 이벤트 객체 전체를 할당

                // 날짜 형식 변환이 필요한 경우
                val formattedDate = DateTimeUtils.formatDateTimeString(event.startTime)
                eventDate.text = formattedDate

                executePendingBindings()  // 바인딩 즉시 적용
            }

            binding.root.setOnClickListener { onEventClick(event) }
        }
    }
}
