package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.BlockImageItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CreateBlockImagesAdapter(
    private val viewModel: CreateCurationViewModel,
    private val blockPosition: Int
) : ListAdapter<ImageResponse, CreateBlockImagesAdapter.ImageViewHolder>(ImageDiffCallback) {
    fun CreateBlockImagesAdapter.unregisterAllObservers() {
        unregisterAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {})
    }
    companion object {
        private object ImageDiffCallback : DiffUtil.ItemCallback<ImageResponse>() {
            override fun areItemsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    // setImages 함수는 ListAdapter의 submitList를 사용
    fun setImages(newImages: List<ImageResponse>) {
        submitList(newImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = BlockImageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ImageViewHolder(
        private val binding: BlockImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageResponse, position: Int) {
            binding.apply {
                this.image = image
                this.viewModel = this@CreateBlockImagesAdapter.viewModel
                this.blockPosition = this@CreateBlockImagesAdapter.blockPosition
                this.imagePosition = position

                executePendingBindings()
            }
        }
    }
}