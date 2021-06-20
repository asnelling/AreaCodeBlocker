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

class PrefixListAdapter :
    ListAdapter<Prefix, PrefixListAdapter.PrefixViewHolder>(
        PrefixComparator()
    ) {

    var removeListener = RemovePrefixListener { }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrefixViewHolder = PrefixViewHolder.create(parent).apply {
        removePrefixButton.setOnClickListener { v ->
            val prefix = v.tag as Prefix
            Log.i(TAG, "remove button clicked: ${prefix.id} ${prefix.number}")
            removeListener.remove(prefix)
        }
    }

    override fun onBindViewHolder(holder: PrefixViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun interface RemovePrefixListener {
        fun remove(prefix: Prefix)
    }

    class PrefixViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionImage: ImageView =
            itemView.findViewById(R.id.action_image)
        private val prefixItemView: TextView =
            itemView.findViewById(R.id.prefix)
        val removePrefixButton: ImageButton = itemView.findViewById(R.id.remove)

        fun bind(prefix: Prefix) {
            prefixItemView.text = prefix.number
            removePrefixButton.tag = prefix

            when (prefix.action) {
                Action.DISALLOW -> {
                    actionImage.apply {
                        setImageResource(R.drawable.ic_baseline_block_24)
                        contentDescription = itemView
                            .resources
                            .getString(R.string.call_disallow)
                    }
                }
                Action.REJECT -> {
                    actionImage.apply {
                        setImageResource(R.drawable.ic_baseline_phone_missed_24)
                        contentDescription = itemView
                            .resources
                            .getString(R.string.call_reject)
                    }
                }
                Action.SILENCE -> {
                    actionImage.apply {
                        setImageResource(
                            R.drawable.ic_baseline_notifications_off_24
                        )
                        contentDescription = itemView
                            .resources
                            .getString(R.string.call_silence)
                    }
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
        override fun areItemsTheSame(a: Prefix, b: Prefix): Boolean =
            a.id == b.id

        override fun areContentsTheSame(a: Prefix, b: Prefix): Boolean =
            a == b
    }

    companion object {
        private const val TAG = "PrefixListAdapter"
    }
}