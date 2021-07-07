package io.asnell.prefixscreener

import androidx.annotation.WorkerThread
import io.asnell.prefixscreener.db.AppDatabase
import io.asnell.prefixscreener.db.History
import io.asnell.prefixscreener.db.Prefix
import kotlinx.coroutines.flow.Flow

class Repository(private val database: AppDatabase) {
    val allPrefixes: Flow<List<Prefix>> = database.prefixDao().getPrefixes()

    fun matchingPrefixes(number: String): Flow<List<Prefix>> =
        database.prefixDao().getMatching(number)

    val allHistory: Flow<List<History>> = database.historyDao().getHistory()

    @WorkerThread
    suspend fun insert(prefix: Prefix) {
        database.prefixDao().insert(prefix)
    }

    @WorkerThread
    suspend fun delete(prefix: Prefix) {
        database.prefixDao().delete(prefix)
    }

    @WorkerThread
    suspend fun insert(history: History) {
        database.historyDao().insert(history)
    }
}