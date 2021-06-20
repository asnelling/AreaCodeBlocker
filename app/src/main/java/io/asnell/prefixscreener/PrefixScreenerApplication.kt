package io.asnell.prefixscreener

import android.app.Application
import io.asnell.prefixscreener.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PrefixScreenerApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val repository by lazy {
        Repository(
            AppDatabase.getDatabase(this)
        )
    }
}