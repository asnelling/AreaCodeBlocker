package io.asnell.prefixscreener

import android.app.Application
import io.asnell.prefixscreener.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PrefixScreenerApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy {
        AppDatabase.getDatabase(
            this,
            applicationScope
        )
    }
    val repository by lazy { Repository(database) }
}