package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.core.DateTimeUtils
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.SearchItemBinding

class EventSearchAdapter(
    private var onEventClick: (EventResponse) -> Unit
) : ListAdapter<EventResponse, EventSearchAdapter.EventViewHolder>(EventDiffCallback()) {

    fun setOnEventClickListener(listener: (EventResponse) -> Unit) {
        onEventClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener { onEventClick(event) }
    }

    inner class EventViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventResponse) {
            binding.apply {
                eventTitle.text = event.title
                val eventTime = DateTimeUtils.formatDateTimeString(event.startTime)
                eventDate.text = eventTime
                eventDescription.text = event.content
                imageUrl = event.thumbnailUrl
            }

            itemView.setOnClickListener {
                onEventClick.invoke(event)
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<EventResponse>() {
        override fun areItemsTheSame(oldItem: EventResponse, newItem: EventResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EventResponse, newItem: EventResponse): Boolean {
            return oldItem == newItem
        }
    }
}
