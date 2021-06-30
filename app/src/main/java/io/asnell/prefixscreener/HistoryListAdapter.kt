package io.asnell.prefixscreener

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.History

class HistoryListAdapter :
    ListAdapter<History, HistoryListAdapter.HistoryViewHolder>(HistoryComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder = HistoryViewHolder.create(parent)

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int
    ) {
        val current = getItem(position)
        holder.bind(current)
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val callerNumberView: TextView =
            view.findViewById(R.id.caller_number)

        private val screenedAtView: TextView =
            view.findViewById(R.id.screened_at)

        private val screenedActionIcon: ImageView =
            view.findViewById(R.id.screened_action)

        fun bind(history: History) {
            callerNumberView.text = history.callerNumber
            screenedAtView.text = DateUtils.getRelativeDateTimeString(
                itemView.context,
                history.receivedAt,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_NO_MONTH_DAY
            )

            screenedActionIcon.apply {
                when (history.result) {
                    Action.DISALLOW.name -> {
                        setImageResource(R.drawable.ic_baseline_block_24)
                        contentDescription =
                            itemView.resources.getString(R.string.history_disallowed)
                    }
                    Action.REJECT.name -> {
                        setImageResource(R.drawable.ic_baseline_phone_missed_24)
                        contentDescription =
                            itemView.resources.getString(R.string.history_rejected)
                    }
                    Action.SILENCE.name -> {
                        setImageResource(R.drawable.ic_baseline_notifications_off_24)
                        contentDescription =
                            itemView.resources.getString(R.string.history_silenced)
                    }
                    "allow" -> {
                        setImageResource(R.drawable.ic_baseline_call_received_24)
                        contentDescription =
                            itemView.resources.getString(R.string.history_allowed)
                    }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): HistoryViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.history_list_item, parent, false)
                return HistoryViewHolder(view)
            }
        }
    }

    class HistoryComparator : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(
            a: History,
            b: History
        ): Boolean = a.id == b.id

        override fun areContentsTheSame(
            oldItem: History,
            newItem: History
        ): Boolean = oldItem == newItem
    }
}
