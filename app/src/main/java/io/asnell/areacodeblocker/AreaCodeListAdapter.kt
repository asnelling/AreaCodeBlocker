package io.asnell.areacodeblocker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AreaCodeListAdapter : ListAdapter<AreaCode, AreaCodeListAdapter.AreaCodeViewHolder>(AreaCodesComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaCodeViewHolder {
        return AreaCodeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AreaCodeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.code)
    }

    class AreaCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val areaCodeItemView: TextView = itemView.findViewById(R.id.areaCode)

        fun bind(text: String?) {
            areaCodeItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): AreaCodeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return AreaCodeViewHolder(view)
            }
        }
    }

    class AreaCodesComparator : DiffUtil.ItemCallback<AreaCode>() {
        override fun areItemsTheSame(oldItem: AreaCode, newItem: AreaCode): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AreaCode, newItem: AreaCode): Boolean {
            return oldItem.code == newItem.code
        }
    }
}