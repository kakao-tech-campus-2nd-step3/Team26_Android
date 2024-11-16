package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.CurationItemBinding

class CurationCardAdapter(
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CurationCardAdapter.CurationCardViewHolder>() {

    private var items: List<CurationResponse> = emptyList()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationCardViewHolder {
        val binding = CurationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurationCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurationCardViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    // 데이터 업데이트
    fun setData(curations: List<CurationResponse>) {
        this.items = curations
        notifyDataSetChanged()  // 데이터 갱신 후 뷰 업데이트
    }

    inner class CurationCardViewHolder(private val binding: CurationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.apply {
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    Log.d("ViewHolder", "View clicked: ${this.id}")
                    Log.d("ViewHolder", "Position: $absoluteAdapterPosition")
                    Log.d("ViewHolder", "Is clickable: ${this.isClickable}")
                    Log.d("ViewHolder", "Is enabled: ${this.isEnabled}")
                    Log.d("ViewHolder", "Clicked at position: $absoluteAdapterPosition")
                    if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(absoluteAdapterPosition)
                    }
                }
            }
        }

        fun bind(curation: CurationResponse, position: Int) {
            binding.curation = curation
            binding.position = position
            binding.imageUrl = curation.thumbnailUrl

            binding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )

            val hashtags = curation.hashtags.getOrNull(0)
            if (!hashtags.isNullOrEmpty()) {
                binding.hashtag.text = "#${hashtags}"
            }

            binding.executePendingBindings()
        }
    }
}

