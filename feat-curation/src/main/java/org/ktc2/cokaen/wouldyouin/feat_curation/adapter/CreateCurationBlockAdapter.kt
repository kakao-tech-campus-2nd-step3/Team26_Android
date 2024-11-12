package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
            // 같은 위치의 블록인지만 확인
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Block, newItem: Block): Boolean {
            // 실제 내용 비교
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

        fun bind(block: Block, position: Int) {
            removeTextWatchers()

            binding.apply {
                this.viewModel = this@CreateCurationBlockAdapter.viewModel
                this.position = position

                etBlockTitle.setText(block.title)
                etBlockContent.setText(block.body)

                setupTextWatchers(position)

                executePendingBindings()
            }
        }

        private fun setupTextWatchers(position: Int) {
            titleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    titleUpdateJob?.cancel()
                    titleUpdateJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500) // 500ms 디바운스
                        this@CreateCurationBlockAdapter.viewModel?.updateBlockTitle(position, s.toString())
                    }
                }
            }

            contentTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    contentUpdateJob?.cancel()
                    contentUpdateJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // 500ms 디바운스
                        this@CreateCurationBlockAdapter.viewModel?.updateBlockBody(position, s.toString())
                    }
                }
            }

            binding.etBlockTitle.addTextChangedListener(titleTextWatcher)
            binding.etBlockContent.addTextChangedListener(contentTextWatcher)
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