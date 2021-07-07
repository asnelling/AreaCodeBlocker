package io.asnell.prefixscreener.db

import android.telecom.CallScreeningService.CallResponse
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.asnell.prefixscreener.R

@Entity
data class Prefix(
    val number: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val action: Action = Action.DISALLOW,
)

enum class Action {
    DISALLOW {
        override fun responseBuilder(builder: CallResponse.Builder): CallResponse.Builder =
            with(builder) {
                setDisallowCall(true)
            }

        override fun setupIcon(icon: ImageView) = with(icon) {
            setImageResource(R.drawable.ic_baseline_block_24)
            contentDescription = resources.getString(R.string.history_disallowed)
        }
    },
    REJECT {
        override fun responseBuilder(builder: CallResponse.Builder): CallResponse.Builder =
            with(builder) {
                setDisallowCall(true)
                setRejectCall(true)
            }

        override fun setupIcon(icon: ImageView) = with(icon) {
            setImageResource(R.drawable.ic_baseline_phone_missed_24)
            contentDescription = resources.getString(R.string.history_rejected)
        }
    },
    SILENCE {
        override fun responseBuilder(builder: CallResponse.Builder): CallResponse.Builder =
            with(builder) {
                setSilenceCall(true)
            }

        override fun setupIcon(icon: ImageView) = with(icon) {
            setImageResource(R.drawable.ic_baseline_notifications_off_24)
            contentDescription = resources.getString(R.string.history_silenced)
        }
    },
    ALLOW {
        override fun responseBuilder(builder: CallResponse.Builder): CallResponse.Builder =
            builder

        override fun setupIcon(icon: ImageView) = with(icon) {
            setImageResource(R.drawable.ic_baseline_call_received_24)
            contentDescription = resources.getString(R.string.history_allowed)
        }
    };

    abstract fun responseBuilder(builder: CallResponse.Builder): CallResponse.Builder

    fun makeResponse(): CallResponse =
        responseBuilder(CallResponse.Builder()).build()

    abstract fun setupIcon(icon: ImageView)
}
