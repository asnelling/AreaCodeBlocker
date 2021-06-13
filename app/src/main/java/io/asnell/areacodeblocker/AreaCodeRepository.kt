package io.asnell.areacodeblocker

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AreaCodeRepository(private val areaCodeDao: AreaCodeDao) {
    val allAreaCodes: Flow<List<AreaCode>> = areaCodeDao.getAreaCodes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(areaCode: AreaCode) {
        areaCodeDao.insert(areaCode)
    }
}