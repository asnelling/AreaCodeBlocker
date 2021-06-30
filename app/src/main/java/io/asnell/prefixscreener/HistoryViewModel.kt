package io.asnell.prefixscreener

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.asnell.prefixscreener.db.History
import kotlinx.coroutines.flow.Flow

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = (application as PrefixScreenerApplication).repository

    val allHistory: Flow<List<History>> = repository.allHistory
}