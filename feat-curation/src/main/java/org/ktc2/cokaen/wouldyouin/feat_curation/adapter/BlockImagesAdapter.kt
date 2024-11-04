package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.BlockImageItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class BlockImagesAdapter(
    private val viewModel: CreateCurationViewModel,
    private val blockPosition: Int
) : RecyclerView.Adapter<BlockImagesAdapter.ImageViewHolder>() {

    private var images: List<String> = emptyList()

    fun setImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = BlockImageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(
        private val binding: BlockImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String, position: Int) {
            binding.apply {
                this.imageUrl = imageUrl
                this.viewModel = this@BlockImagesAdapter.viewModel
                this.blockPosition = this@BlockImagesAdapter.blockPosition
                this.imagePosition = position
                executePendingBindings()
            }
        }
    }
}