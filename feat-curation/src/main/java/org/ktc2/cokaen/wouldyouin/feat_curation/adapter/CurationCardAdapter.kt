package org.ktc2.cokaen.wouldyouin.feat_curation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ktc2.cokaen.wouldyouin.data.model.Curation
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.CurationItemBinding

class CurationCardAdapter(
    private var items: List<Curation>,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<CurationCardAdapter.CurationCardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationCardViewHolder {
        val binding = CurationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurationCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurationCardViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun setData(searchResults: List<Curation>) {
        items = searchResults
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Curation = items[position]

    inner class CurationCardViewHolder(private val binding: CurationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(curation: Curation, position: Int) {
            binding.curation = curation
            binding.position = position
            binding.executePendingBindings()
        }
    }
}