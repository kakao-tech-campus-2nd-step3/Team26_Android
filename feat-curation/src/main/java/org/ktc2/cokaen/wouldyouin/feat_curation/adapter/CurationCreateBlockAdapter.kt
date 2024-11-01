package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.AddCurationBlockItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CurationCreateBlockAdapter(
    private val viewModel: CreateCurationViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_BLOCK = 0
        private const val TYPE_ADD_BUTTON = 1
    }

    private var items: List<Block> = emptyList()

    fun setItems(newItems: List<Block>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) TYPE_ADD_BUTTON else TYPE_BLOCK
    }

    override fun getItemCount(): Int = items.size + 1  // 아이템 개수 + 추가 버튼

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BLOCK -> {
                val binding = AddCurationBlockItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                CurationBlockViewHolder(binding)
            }
            TYPE_ADD_BUTTON -> {
                val binding = AddCurationBlockItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AddButtonViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CurationBlockViewHolder -> {
                val block = items[position]
                holder.bind(block)
            }
            is AddButtonViewHolder -> {
                holder.bind()
            }
        }
    }

    inner class CurationBlockViewHolder(
        private val binding: AddCurationBlockItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(block: Block) {
            binding.block = block
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    inner class AddButtonViewHolder(
        private val binding: AddCurationBlockItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}