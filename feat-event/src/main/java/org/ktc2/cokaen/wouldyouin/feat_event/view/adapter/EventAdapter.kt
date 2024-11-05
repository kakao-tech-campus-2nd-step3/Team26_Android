package org.ktc2.cokaen.wouldyouin.feat_event.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.CategoryItemBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.EventDetailActivity

class EventAdapter(private val events: List<EventRequest>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventRequest) {
            binding.eventTitle.text = event.title
            binding.eventDate.text = event.startTime
            binding.eventLocation.text = event.location.name
            binding.eventFee.text = "입장료 ${event.price}₩"
            binding.eventSeats.text = "${event.totalSeats}"
            binding.eventDescription.text = event.content

            if (event.eventImages.isNotEmpty()) {
                Glide.with(binding.eventImage.context)
                    .load(event.eventImages[0])
                    .into(binding.eventImage)
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("event_title", event.title)
                    putExtra("event_startTime", event.startTime)
                    putExtra("event_endTime", event.endTime)
                    putExtra("event_location", event.location.name)
                    putExtra("event_price", event.price.toString())
                    putExtra("event_totalSeats", event.totalSeats.toString())
                    putExtra("event_description", event.content)
                    if (event.eventImages.isNotEmpty()) {
                        putExtra("event_image", event.eventImages[0])
                    }
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
