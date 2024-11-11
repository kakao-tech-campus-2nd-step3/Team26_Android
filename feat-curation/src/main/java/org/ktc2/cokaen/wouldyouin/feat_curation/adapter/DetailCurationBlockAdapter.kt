package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.data.model.CurationCardResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemDetailCurationBlockBinding

class DetailCurationBlockAdapter : ListAdapter<CurationCardResponse, DetailCurationBlockAdapter.CurationBlockViewHolder>(
    object : DiffUtil.ItemCallback<CurationCardResponse>() {
        override fun areItemsTheSame(oldItem: CurationCardResponse, newItem: CurationCardResponse): Boolean {
            return oldItem.imageUrls == newItem.imageUrls
        }

        override fun areContentsTheSame(oldItem: CurationCardResponse, newItem: CurationCardResponse): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationBlockViewHolder {
        val binding = ItemDetailCurationBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurationBlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurationBlockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurationBlockViewHolder(private val binding: ItemDetailCurationBlockBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(block: CurationCardResponse) {
            binding.tvBlockTitle.text = block.title
            binding.tvBlockContent.text = block.body

            val imageAdapter = DetailCurationImageAdapter(block.imageUrls)
            binding.rvBlockImages.adapter = imageAdapter
            binding.rvBlockImages.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
