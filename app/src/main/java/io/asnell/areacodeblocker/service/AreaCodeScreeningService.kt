package io.asnell.areacodeblocker.service

import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.CallScreeningService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import io.asnell.areacodeblocker.Action
import io.asnell.areacodeblocker.AreaCode
import io.asnell.areacodeblocker.AreaCodesApplication

class AreaCodeScreeningService : CallScreeningService() {
    private val blocklist: LiveData<List<AreaCode>> by lazy {
        (application as AreaCodesApplication).repository
            .allAreaCodes
            .asLiveData()
    }

    private var blocklistCache: List<AreaCode> = emptyList()
    private val repoObserver: Observer<List<AreaCode>> = Observer { list ->
        blocklistCache = list
    }


    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != DIRECTION_INCOMING) {
            return
        }

        // The handle (e.g., phone number) to which the Call is currently
        // connected.
        val handle = callDetails.handle
        val callerNumber = handle.schemeSpecificPart
        Log.d(TAG, "call from: $handle [$callerNumber]")

        val response = CallResponse.Builder()

        for (areaCode in blocklistCache) {
            if (callerNumber.startsWith(areaCode.code)) {
                when (areaCode.action) {
                    Action.DISALLOW -> {
                        Log.d(TAG, "disallowing call")
                        response.setDisallowCall(true)
                    }
                    Action.REJECT -> {
                        Log.d(TAG, "rejecting call")
                        response.setDisallowCall(true)
                        response.setRejectCall(true)
                    }
                    Action.SILENCE -> {
                        Log.d(TAG, "silencing call")
                        response.setSilenceCall(true)
                    }
                }
                break
            }
        }

        respondToCall(callDetails, response.build())
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service created")
        blocklist.observeForever(repoObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "service destroyed")
        blocklist.removeObserver(repoObserver)
    }

    companion object {
        private const val TAG = "AreaCodeScreeningService"
    }
}