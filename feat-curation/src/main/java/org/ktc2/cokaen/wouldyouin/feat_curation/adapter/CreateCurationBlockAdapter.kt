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
        private var updateJob: Job? = null
        private var imagesAdapter: CreateBlockImagesAdapter? = null

        fun bind(block: Block, position: Int) {
            removeTextWatchers()

            binding.apply {
                this.viewModel = this@CreateCurationBlockAdapter.viewModel
                this.position = position

                // 기존 텍스트 설정
                etBlockTitle.setText(block.title)
                etBlockContent.setText(block.body)

                // 이미지 어댑터 설정 및 검증
                setupImagesAdapter(position, block)

                // 유효성 검사 설정
                setupValidation(position)

                executePendingBindings()
            }
        }

        private fun setupImagesAdapter(position: Int, block: Block) {
            // 어댑터가 없는 경우에만 새로 생성
            if (imagesAdapter == null) {
                imagesAdapter = CreateBlockImagesAdapter(viewModel, position).apply {
                    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                            super.onItemRangeInserted(positionStart, itemCount)
                            val totalImages = currentList.size
                            if (totalImages > 5) {
                                viewModel.showError("이미지는 5개까지만 추가할 수 있습니다.")
                                viewModel.handleImageDelete(position, currentList.last())
                            }
                        }
                    })
                }
                binding.rvImages.adapter = imagesAdapter
            }

            // 이미지 리스트 업데이트
            imagesAdapter?.submitList(block.images)
        }

        private fun setupValidation(position: Int) {
            // 제목 유효성 검사
            binding.etBlockTitle.addValidationWatcher { title ->
                if (title.isBlank()) {
                    binding.etBlockTitle.error = "제목은 필수입니다"
                    false
                } else {
                    binding.etBlockTitle.error = null
                    viewModel.updateBlockTitle(position, title)
                    true
                }
            }

            // 본문 유효성 검사
            binding.etBlockContent.addValidationWatcher { content ->
                when {
                    content.length < 20 -> {
                        binding.etBlockContent.error = "본문은 20자 이상이어야 합니다"
                        false
                    }
                    content.length > 1000 -> {
                        binding.etBlockContent.error = "본문은 1000자 이하여야 합니다"
                        false
                    }
                    else -> {
                        binding.etBlockContent.error = null
                        viewModel.updateBlockBody(position, content)
                        true
                    }
                }
            }
        }

        private fun EditText.addValidationWatcher(validate: (String) -> Boolean): TextWatcher {
            return object : TextWatcher {
                private var cursorPosition = 0

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    cursorPosition = selectionStart
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val currentText = s.toString()
                    updateJob?.cancel()
                    updateJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500) // 디바운싱
                        validate(currentText)
                        // 커서 위치 복원
                        setSelection(cursorPosition.coerceAtMost(currentText.length))
                    }
                }
            }.also { addTextChangedListener(it) }
        }

        fun unbind() {
            removeTextWatchers()
            updateJob?.cancel()
            binding.apply {
                etBlockTitle.onFocusChangeListener = null
                etBlockContent.onFocusChangeListener = null
                // 이미지 어댑터는 유지하고 리스너만 제거
                imagesAdapter?.unregisterAllObservers()
            }
        }

        fun CreateBlockImagesAdapter.unregisterAllObservers() {
            unregisterAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {})
        }

        private fun removeTextWatchers() {
            titleTextWatcher?.let { binding.etBlockTitle.removeTextChangedListener(it) }
            contentTextWatcher?.let { binding.etBlockContent.removeTextChangedListener(it) }
            titleTextWatcher = null
            contentTextWatcher = null
        }
    }
}