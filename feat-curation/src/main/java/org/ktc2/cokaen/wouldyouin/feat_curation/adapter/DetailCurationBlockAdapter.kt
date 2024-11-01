package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemDetailCurationBlockBinding

class DetailCurationBlockAdapter : ListAdapter<Block, DetailCurationBlockAdapter.CurationBlockViewHolder>(CurationBlockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationBlockViewHolder {
        val binding = ItemDetailCurationBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurationBlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurationBlockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurationBlockViewHolder(private val binding: ItemDetailCurationBlockBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(block: Block) {
            binding.tvBlockTitle.text = block.title
            binding.tvBlockContent.text = block.body

            val imageAdapter = DetailCurationImageAdapter(block.images)
            binding.rvBlockImages.adapter = imageAdapter
            binding.rvBlockImages.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}

class CurationBlockDiffCallback : DiffUtil.ItemCallback<Block>() {
    override fun areItemsTheSame(oldItem: Block, newItem: Block): Boolean {
        // Block 클래스에 적절한 ID 필드가 있다면 해당 필드를 비교하도록 수정하세요.
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Block, newItem: Block): Boolean {
        return oldItem == newItem
    }
}