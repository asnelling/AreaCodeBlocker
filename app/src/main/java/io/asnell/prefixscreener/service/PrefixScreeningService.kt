package io.asnell.prefixscreener.service

import android.telecom.Call
import android.telecom.Call.Details.DIRECTION_INCOMING
import android.telecom.CallScreeningService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix
import io.asnell.prefixscreener.PrefixScreenerApplication
import io.asnell.prefixscreener.Repository
import io.asnell.prefixscreener.db.History
import kotlinx.coroutines.*

class PrefixScreeningService : CallScreeningService() {
    private val applicationScope: CoroutineScope by lazy {
        (application as PrefixScreenerApplication).applicationScope
    }
    private val repository: Repository by lazy {
        (application as PrefixScreenerApplication).repository
    }
    private val blocklist: LiveData<List<Prefix>> by lazy {
        repository
            .allPrefixes
            .asLiveData()
    }

    private var blocklistCache: List<Prefix> = emptyList()
    private val repoObserver: Observer<List<Prefix>> = Observer { list ->
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
        var result = "allow"

        for (prefix in blocklistCache) {
            if (callerNumber.startsWith(prefix.number)) {
                when (prefix.action) {
                    Action.DISALLOW -> {
                        Log.d(TAG, "disallowing call")
                        response.setDisallowCall(true)
                        result = "disallow"
                    }
                    Action.REJECT -> {
                        Log.d(TAG, "rejecting call")
                        response.setDisallowCall(true)
                        response.setRejectCall(true)
                        result = "reject"
                    }
                    Action.SILENCE -> {
                        Log.d(TAG, "silencing call")
                        response.setSilenceCall(true)
                        result = "silence"
                    }
                }
                break
            }
        }

        respondToCall(callDetails, response.build())

        applicationScope.launch {
            repository.insert(
                History(
                callDetails.creationTimeMillis,
                callerNumber,
                result,
            )
            )
        }
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
        private const val TAG = "PrefixScreeningService"
    }
}