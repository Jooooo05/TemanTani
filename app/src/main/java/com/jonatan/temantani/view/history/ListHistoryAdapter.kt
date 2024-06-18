package com.jonatan.temantani.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jonatan.temantani.databinding.ItemsHistoryBinding

class ListHistoryAdapter(
    private val onItemClickCallback: (HistoryEntity) -> Unit = {}
) : RecyclerView.Adapter<ListHistoryAdapter.HistoryViewHolder>() {

    var listHistory: List<HistoryEntity> = emptyList()
        set(value) {
            val diffCallback = HistoryDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemsHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = listHistory.size

    inner class HistoryViewHolder(private val binding: ItemsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyEntity: HistoryEntity) {
            with(binding) {
                tvDate.text = historyEntity.date

                root.setOnClickListener {
                    onItemClickCallback(historyEntity)
                }
            }
        }
    }

    private class HistoryDiffCallback(
        private val oldList: List<HistoryEntity>,
        private val newList: List<HistoryEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
