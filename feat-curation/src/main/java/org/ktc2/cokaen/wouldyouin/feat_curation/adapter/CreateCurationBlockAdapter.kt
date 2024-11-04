package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemCurationBlockBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CreateCurationBlockAdapter(
    private val viewModel: CreateCurationViewModel,
    private val deleteClickListener: DeleteClickListener,
) : RecyclerView.Adapter<CreateCurationBlockAdapter.CurationBlockViewHolder>() {

    private var blocks: List<Block> = listOf()

    interface DeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setBlocks(newBlocks: List<Block>) {
        blocks = newBlocks.toList()
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
        holder.bind(position)
    }

    override fun onViewRecycled(holder: CurationBlockViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int = blocks.size

    inner class CurationBlockViewHolder(
        private val binding: ItemCurationBlockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var titleTextWatcher: TextWatcher? = null
        private var contentTextWatcher: TextWatcher? = null

        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.position = position

            // 삭제 버튼 클릭 리스너
            binding.removeCurationBlocks.setOnClickListener {
                deleteClickListener.onDeleteClick(bindingAdapterPosition)
            }

            // 기본 텍스트 설정
            binding.etBlockTitle.setText(viewModel.getBlockTitle(position))
            binding.etBlockContent.setText(viewModel.getBlockBody(position))

            // 포커스 잃을 때 저장
            binding.etBlockTitle.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.etBlockTitle.post {
                        viewModel.updateBlockTitle(position, binding.etBlockTitle.text.toString())
                    }
                }
            }

            binding.etBlockContent.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.etBlockContent.post {
                        viewModel.updateBlockBody(position, binding.etBlockContent.text.toString())
                    }
                }
            }

            binding.executePendingBindings()
        }

        private fun removeTextWatchers() {
            titleTextWatcher?.let { binding.etBlockTitle.removeTextChangedListener(it) }
            contentTextWatcher?.let { binding.etBlockContent.removeTextChangedListener(it) }
            titleTextWatcher = null
            contentTextWatcher = null
        }

        fun unbind() {
            removeTextWatchers()
        }
    }
}