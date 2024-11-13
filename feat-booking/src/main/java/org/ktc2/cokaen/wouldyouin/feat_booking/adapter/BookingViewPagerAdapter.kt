package org.ktc2.cokaen.wouldyouin.feat_booking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.BookingCardItemBinding

class BookingViewPagerAdapter(private val items: List<ReservationResponse>) :
    RecyclerView.Adapter<BookingViewPagerAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = BookingCardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewPagerViewHolder(private val binding: BookingCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReservationResponse) {
            binding.apply {
                eventDate.text = item.event.date
                eventName.text = item.event.title
                eventLocation.text = item.event.location.detailAddress
                imageUrl = item.event.imageUrl
                ticketCount.text = item.quantity.toString() + ", ₩" + item.price
                reservationId.text = item.id.toString()
            }
        }
    }
}
