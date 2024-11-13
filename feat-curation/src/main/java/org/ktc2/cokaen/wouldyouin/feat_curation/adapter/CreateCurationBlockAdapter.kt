package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemCurationBlockBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CreateCurationBlockAdapter(
    private val viewModel: CreateCurationViewModel,
    private val deleteClickListener: (Int) -> Unit,
) : ListAdapter<Block, CreateCurationBlockAdapter.CurationBlockViewHolder>(BlockDiffCallback) {

    private object BlockDiffCallback : DiffUtil.ItemCallback<Block>() {
        override fun areItemsTheSame(oldItem: Block, newItem: Block): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Block, newItem: Block): Boolean {
            return oldItem == newItem
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
        // 로그 추가
        val block = getItem(position)
        Log.d("Adapter_Bind", "Binding position $position with block: $block")
        holder.bind(block, position)
    }

    override fun onViewRecycled(holder: CurationBlockViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    inner class CurationBlockViewHolder(
        private val binding: ItemCurationBlockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var titleTextWatcher: TextWatcher? = null
        private var contentTextWatcher: TextWatcher? = null
        private var titleUpdateJob: Job? = null
        private var contentUpdateJob: Job? = null
        private var updateJob: Job? = null

        private var lastKnownTitleCursorPosition = 0
        private var lastKnownContentCursorPosition = 0


        fun bind(block: Block, position: Int) {
            removeTextWatchers()

            binding.apply {
                this.viewModel = this@CreateCurationBlockAdapter.viewModel
                this.position = position

                etBlockTitle.setText(block.title)
                etBlockContent.setText(block.body)

                setupTextWatchers(position)
                setupFocusListeners(position)

                executePendingBindings()
            }
        }

        private fun setupTextWatchers(position: Int) {
            titleTextWatcher = createTextWatcher(binding.etBlockTitle, position, true)
            contentTextWatcher = createTextWatcher(binding.etBlockContent, position, false)

            binding.etBlockTitle.addTextChangedListener(titleTextWatcher)
            binding.etBlockContent.addTextChangedListener(contentTextWatcher)
        }

        private fun createTextWatcher(editText: EditText, position: Int, isTitle: Boolean): TextWatcher {
            return object : TextWatcher {
                private var cursorPosition = 0

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    cursorPosition = editText.selectionStart
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val currentText = s.toString()
                    updateJob?.cancel()
                    updateJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // 디바운싱을 위한 지연
                        if (isTitle) {
                            viewModel.updateBlockTitle(position, currentText)
                        } else {
                            viewModel.updateBlockBody(position, currentText)
                        }
                        // 커서 위치 복원
                        editText.setSelection(cursorPosition.coerceAtMost(currentText.length))
                    }
                }
            }
        }

        private fun setupFocusListeners(position: Int) {
            binding.etBlockTitle.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    this@CreateCurationBlockAdapter.viewModel?.updateBlockTitle(position, binding.etBlockTitle.text.toString())
                    binding.etBlockTitle.setSelection(lastKnownTitleCursorPosition)
                } else {
                    lastKnownTitleCursorPosition = binding.etBlockTitle.selectionStart
                }
            }
            binding.etBlockContent.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    this@CreateCurationBlockAdapter.viewModel?.updateBlockBody(position, binding.etBlockContent.text.toString())
                    binding.etBlockTitle.setSelection(lastKnownContentCursorPosition)
                } else {
                    lastKnownContentCursorPosition = binding.etBlockContent.selectionStart
                }
            }
        }

        private fun removeTextWatchers() {
            titleTextWatcher?.let { binding.etBlockTitle.removeTextChangedListener(it) }
            contentTextWatcher?.let { binding.etBlockContent.removeTextChangedListener(it) }
            titleTextWatcher = null
            contentTextWatcher = null
            titleUpdateJob?.cancel()
            contentUpdateJob?.cancel()
        }

        fun unbind() {
            removeTextWatchers()
            binding.etBlockTitle.onFocusChangeListener = null
            binding.etBlockContent.onFocusChangeListener = null
        }
    }
}