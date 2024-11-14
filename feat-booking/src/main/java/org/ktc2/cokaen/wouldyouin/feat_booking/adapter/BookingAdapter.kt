package org.ktc2.cokaen.wouldyouin.feat_booking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.feat_booking.databinding.ItemReservationBinding

class BookingAdapter(
    private val reservations: List<ReservationResponse>
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    private var onItemClickListener: ((ReservationResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (ReservationResponse) -> Unit) {
        onItemClickListener = listener
    }

    inner class BookingViewHolder(private val binding: ItemReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reservation: ReservationResponse) {
            binding.eventTitle.text = reservation.event.title
            binding.reservationDate.text = reservation.reservationDate.toString()
            binding.eventDate.text = reservation.event.startTime.toString()
            binding.imageUrl = reservation.event.thumbnailUrl

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(reservation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    override fun getItemCount(): Int = reservations.size
}
