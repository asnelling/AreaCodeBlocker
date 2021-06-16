package io.asnell.areacodeblocker

import android.app.Application
import io.asnell.areacodeblocker.db.AreaCodeRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AreaCodesApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy {
        AreaCodeRoomDatabase.getDatabase(
            this,
            applicationScope
        )
    }
    val repository by lazy { Repository(database) }
}