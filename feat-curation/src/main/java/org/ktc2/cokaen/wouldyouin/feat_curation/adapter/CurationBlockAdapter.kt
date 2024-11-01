package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.CurationItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemCurationBlockBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CurationBlockAdapter(
    private val viewModel: CreateCurationViewModel,
    private val deleteClickListener: DeleteClickListener,
    private val textChangeListener: TextChangeListener
) : RecyclerView.Adapter<CurationBlockAdapter.CurationBlockViewHolder>() {

    private var blocks: MutableList<Block> = mutableListOf()

    interface DeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    interface TextChangeListener {
        fun onTitleChanged(position: Int, newTitle: String)
        fun onBodyChanged(position: Int, newBody: String)
    }

    fun setItems(newItems: List<Block>) {
        val oldItemCount = blocks.size
        blocks.clear()
        blocks.addAll(newItems)
        if (oldItemCount == 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(oldItemCount, newItems.size - oldItemCount)
        }
    }

    fun removeItem(position: Int) {
        if (position in 0 until blocks.size) {
            blocks.removeAt(position)
            notifyItemRemoved(position)
        }
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
        Log.d("CurationBlockAdapter", "onBindViewHolder: position=$position, block=${blocks[position]}")
        holder.bind(position)
    }

    override fun getItemCount(): Int = blocks.size

    inner class CurationBlockViewHolder(
        private val binding: ItemCurationBlockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.position = position
            binding.viewModel = viewModel

            binding.removeCurationBlocks.setOnClickListener {
                deleteClickListener.onDeleteClick(position)
            }

            binding.ivAddImage.setOnClickListener {
                viewModel.onImageClick(position)
            }

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

            binding.etBlockTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textChangeListener.onTitleChanged(position, s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            binding.etBlockContent.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textChangeListener.onBodyChanged(position, s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            binding.executePendingBindings()
        }
    }
}