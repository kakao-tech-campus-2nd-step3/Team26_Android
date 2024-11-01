package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemCurationBlockBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CurationBlockAdapter(
    private val viewModel: CreateCurationViewModel
) : RecyclerView.Adapter<CurationBlockAdapter.CurationBlockViewHolder>() {

    private var blocks: List<Block> = emptyList()

    fun setItems(newItems: List<Block>) {
        blocks = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationBlockViewHolder {
        return CurationBlockViewHolder(
            ItemCurationBlockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CurationBlockViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int = blocks.size

    inner class CurationBlockViewHolder(
        private val binding: ItemCurationBlockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CreateCurationViewModel, position: Int) {
            binding.position = position
            binding.viewModel = viewModel

            // 이미지 RecyclerView 설정
            binding.rvImages.apply {
                if (adapter == null) {
                    adapter = BlockImagesAdapter(viewModel, position)
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
                (adapter as? BlockImagesAdapter)?.setImages(viewModel.getBlockImages(position))
            }

            binding.executePendingBindings()
        }
    }
}