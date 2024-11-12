package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.BlockImageItemBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel

class CreateBlockImagesAdapter(
    private val viewModel: CreateCurationViewModel,
    private val blockPosition: Int
) : RecyclerView.Adapter<CreateBlockImagesAdapter.ImageViewHolder>() {  // ImageDiffCallback 제거

    private var images: List<ImageResponse> = emptyList()  // String -> ImageResponse
    private val MAX_IMAGES = 5  // 최대 이미지 개수를 5로 설정

    fun setImages(newImages: List<ImageResponse>) {
        images = newImages.take(MAX_IMAGES)  // 최대 5개까지만 선택
        notifyDataSetChanged()
    }

    interface OnImageClickListener {
        fun onImageDeleteClick(blockPosition: Int, image: ImageResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = org.ktc2.cokaen.wouldyouin.feat_curation.databinding.BlockImageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount() = minOf(images.size, MAX_IMAGES)

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