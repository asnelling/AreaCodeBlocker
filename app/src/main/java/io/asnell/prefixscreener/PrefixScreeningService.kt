package io.asnell.prefixscreener

import android.os.Build
import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.CallScreeningService
import android.util.Log
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.History
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrefixScreeningService : CallScreeningService() {
    private val app: PrefixScreenerApplication by lazy {
        application as PrefixScreenerApplication
    }

    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != DIRECTION_INCOMING) {
            return
        }

        // The handle (e.g., phone number) to which the Call is currently
        // connected.
        val callerNumber = callDetails.handle.schemeSpecificPart

        app.applicationScope.launch {
            var selectedAction = Action.ALLOW
            val response = CallResponse.Builder()
            val prefixes = app.repository.allPrefixes.first()

            for ((number, _, action) in prefixes) {
                if (callerNumber.startsWith(number)) {
                    selectedAction = action
                    break
                }
            }

            when (selectedAction) {
                Action.DISALLOW -> response.setDisallowCall(true)
                Action.REJECT -> {
                    response.setDisallowCall(true)
                    response.setRejectCall(true)
                }
                Action.SILENCE -> response.setSilenceCall(true)
                Action.ALLOW -> {
                }
            }

            respondToCall(callDetails, response.build())

            val verificationStatus =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    callDetails.callerNumberVerificationStatus
                } else {
                    0
                }

            app.repository.insert(
                History(
                    callDetails.creationTimeMillis,
                    callerNumber,
                    selectedAction.name,
                    verificationStatus,
                )
            )
        }
    }

    companion object {
        private const val TAG = "PrefixScreeningService"
    }
}