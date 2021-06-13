package io.asnell.areacodeblocker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AreaCodeListAdapter : ListAdapter<AreaCode, AreaCodeListAdapter.AreaCodeViewHolder>(AreaCodesComparator()) {
    var removeListener = AreaCodeRemoveListener {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaCodeViewHolder {
        val viewHolder = AreaCodeViewHolder.create(parent)

        viewHolder.removeAreaCodeButton.setOnClickListener { v ->
            val areaCode = v.tag as AreaCode
            Log.d(TAG, "remove button clicked: ${areaCode.id} ${areaCode.code}")
            removeListener.remove(areaCode)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: AreaCodeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun interface AreaCodeRemoveListener {
        fun remove(areaCode: AreaCode)
    }

    class AreaCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val resources = itemView.resources
        private val actionImage: ImageView = itemView.findViewById(R.id.action_image)
        private val areaCodeItemView: TextView = itemView.findViewById(R.id.areaCode)
        val removeAreaCodeButton: ImageButton = itemView.findViewById(R.id.remove)

        fun bind(areaCode: AreaCode) {
            areaCodeItemView.text = areaCode.code
            removeAreaCodeButton.tag = areaCode

            when(areaCode.action) {
                Action.DISALLOW -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_block_24)
                    actionImage.contentDescription = itemView.resources.getString(R.string.call_block)
                }
                Action.REJECT -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_phone_missed_24)
                    actionImage.contentDescription = itemView.resources.getString(R.string.call_reject)
                }
                Action.SILENCE -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_notifications_off_24)
                    actionImage.contentDescription = itemView.resources.getString(R.string.call_silence)
                }
            }
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

    companion object {
        private const val TAG = "AreaCodeListAdapter"
    }
}