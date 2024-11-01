package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
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
        Log.d("CurationBlockAdapter", "setBlocks called with ${newBlocks.size} items")
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
        holder.unbind()  // ViewHolder가 재사용되기 전에 리소스 정리
    }


    override fun getItemCount(): Int = blocks.size

    inner class CurationBlockViewHolder(
        private val binding: ItemCurationBlockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var titleTextWatcher: TextWatcher? = null
        private var contentTextWatcher: TextWatcher? = null

        init {
            // 각 EditText에 대해 터치 이벤트 처리
            binding.etBlockTitle.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                if (event.action == MotionEvent.ACTION_UP) {
                    v.performClick()
                }
                false
            }

            binding.etBlockContent.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                if (event.action == MotionEvent.ACTION_UP) {
                    v.performClick()
                }
                false
            }
        }

        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.position = position

            // 이전 TextWatcher 제거
            removeTextWatchers()

            binding.etBlockTitle.apply {
                setText(viewModel.getBlockTitle(position))
                isFocusableInTouchMode = true
                setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        post {
                            if (titleTextWatcher == null) {
                                titleTextWatcher = object : TextWatcher {
                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        val currentPosition = bindingAdapterPosition
                                        if (currentPosition != RecyclerView.NO_POSITION) {
                                            viewModel.updateBlockTitle(currentPosition, s.toString())
                                        }
                                    }
                                    override fun afterTextChanged(s: Editable?) {}
                                }
                                addTextChangedListener(titleTextWatcher)
                            }
                        }
                    }
                }
            }

            binding.etBlockContent.apply {
                setText(viewModel.getBlockBody(position))
                isFocusableInTouchMode = true
                setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        post {
                            if (contentTextWatcher == null) {
                                contentTextWatcher = object : TextWatcher {
                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        val currentPosition = bindingAdapterPosition
                                        if (currentPosition != RecyclerView.NO_POSITION) {
                                            viewModel.updateBlockBody(currentPosition, s.toString())
                                        }
                                    }
                                    override fun afterTextChanged(s: Editable?) {}
                                }
                                addTextChangedListener(contentTextWatcher)
                            }
                        }
                    }
                }
            }

            binding.removeCurationBlocks.setOnClickListener {
                deleteClickListener.onDeleteClick(bindingAdapterPosition)
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
            binding.etBlockTitle.onFocusChangeListener = null
            binding.etBlockContent.onFocusChangeListener = null
        }
    }
}