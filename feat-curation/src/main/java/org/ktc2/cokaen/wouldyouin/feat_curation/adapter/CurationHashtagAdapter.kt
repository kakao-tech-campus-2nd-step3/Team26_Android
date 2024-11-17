package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ItemHashtagBinding

class CurationHashtagAdapter : RecyclerView.Adapter<CurationHashtagAdapter.HashtagViewHolder>() {

    private var hashtags: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagViewHolder {
        val binding = ItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HashtagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HashtagViewHolder, position: Int) {
        holder.bind(hashtags[position])
    }

    override fun getItemCount(): Int = hashtags.size

    // submitList 메서드 추가
    fun submitList(newHashtags: List<String>) {
        hashtags = newHashtags
        notifyDataSetChanged() // 전체 목록을 갱신
    }

    inner class HashtagViewHolder(private val binding: ItemHashtagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hashtag: String) {
            binding.hashtag = hashtag  // 데이터 바인딩 변수에 직접 할당
            binding.executePendingBindings()  // 바인딩 즉시 실행
        }
    }
}
