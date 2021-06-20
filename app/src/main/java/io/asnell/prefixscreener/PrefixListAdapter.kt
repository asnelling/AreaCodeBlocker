package io.asnell.prefixscreener

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
import io.asnell.prefixscreener.PrefixListAdapter.RemovePrefixListener
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix

class PrefixListAdapter : ListAdapter<Prefix, PrefixListAdapter.PrefixViewHolder>(
    PrefixComparator()
) {
    var removeListener = RemovePrefixListener {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefixViewHolder {
        val viewHolder = PrefixViewHolder.create(parent)

        viewHolder.removePrefixButton.setOnClickListener { v ->
            val prefix = v.tag as Prefix
            Log.d(TAG, "remove button clicked: ${prefix.id} ${prefix.number}")
            removeListener.remove(prefix)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PrefixViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun interface RemovePrefixListener {
        fun remove(prefix: Prefix)
    }

    class PrefixViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionImage: ImageView = itemView.findViewById(R.id.action_image)
        private val prefixItemView: TextView = itemView.findViewById(R.id.prefix)
        val removePrefixButton: ImageButton = itemView.findViewById(R.id.remove)

        fun bind(prefix: Prefix) {
            prefixItemView.text = prefix.number
            removePrefixButton.tag = prefix

            when(prefix.action) {
                Action.DISALLOW -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_block_24)
                    actionImage.contentDescription = itemView.resources.getString(
                        R.string.call_disallow
                    )
                }
                Action.REJECT -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_phone_missed_24)
                    actionImage.contentDescription = itemView.resources.getString(
                        R.string.call_reject
                    )
                }
                Action.SILENCE -> {
                    actionImage.setImageResource(R.drawable.ic_baseline_notifications_off_24)
                    actionImage.contentDescription = itemView.resources.getString(
                        R.string.call_silence
                    )
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): PrefixViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return PrefixViewHolder(view)
            }
        }
    }

    class PrefixComparator : DiffUtil.ItemCallback<Prefix>() {
        override fun areItemsTheSame(oldItem: Prefix, newItem: Prefix): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Prefix, newItem: Prefix): Boolean {
            return oldItem.number == newItem.number
        }
    }

    companion object {
        private const val TAG = "PrefixListAdapter"
    }
}