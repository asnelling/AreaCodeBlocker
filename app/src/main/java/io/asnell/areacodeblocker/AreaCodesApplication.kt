package io.asnell.areacodeblocker

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AreaCodesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy {
        AreaCodeRoomDatabase.getDatabase(
            this,
            applicationScope
        )
    }
    val repository by lazy { AreaCodeRepository(database.areaCodeDao()) }
}