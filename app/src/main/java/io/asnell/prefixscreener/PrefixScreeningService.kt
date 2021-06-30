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
            var result = "allow"
            val response = CallResponse.Builder()
            val prefixes = app.repository.allPrefixes.first()

            for ((number, _, action) in prefixes) {
                if (callerNumber.startsWith(number)) {
                    when (action) {
                        Action.DISALLOW -> {
                            response.setDisallowCall(true)
                            result = Action.DISALLOW.name
                        }
                        Action.REJECT -> {
                            response.setDisallowCall(true)
                            response.setRejectCall(true)
                            result = Action.REJECT.name
                        }
                        Action.SILENCE -> {
                            response.setSilenceCall(true)
                            result = Action.SILENCE.name
                        }
                    }
                    break
                }
            }

            respondToCall(callDetails, response.build())

            Log.i(TAG, "call from: $callerNumber - screening result: $result")
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
                    result,
                    verificationStatus,
                )
            )
        }
    }

    companion object {
        private const val TAG = "PrefixScreeningService"
    }
}