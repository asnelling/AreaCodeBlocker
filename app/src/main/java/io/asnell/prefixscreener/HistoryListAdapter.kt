package io.asnell.prefixscreener

import android.telecom.Connection.VERIFICATION_STATUS_FAILED
import android.telecom.Connection.VERIFICATION_STATUS_PASSED
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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

            val drawable = when (history.callerNumberVerificationStatus) {
                VERIFICATION_STATUS_PASSED -> ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.ic_baseline_verification_passed_24,
                    null
                )
                VERIFICATION_STATUS_FAILED -> ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.ic_baseline_verification_failed_24,
                    null
                )
                else -> null
            }
            callerNumberView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )

            screenedAtView.text = DateUtils.getRelativeDateTimeString(
                itemView.context,
                history.receivedAt,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_NO_MONTH_DAY
            )

            history.result.setupIcon(screenedActionIcon)
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
