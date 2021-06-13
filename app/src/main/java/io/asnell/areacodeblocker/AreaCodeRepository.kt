package io.asnell.areacodeblocker

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AreaCodeRepository(private val areaCodeDao: AreaCodeDao) {
    val allAreaCodes: Flow<List<AreaCode>> = areaCodeDao.getAreaCodes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(areaCode: AreaCode) {
        areaCodeDao.insert(areaCode)
    }

    @WorkerThread
    suspend fun delete(areaCode: AreaCode) {
        Log.d(TAG, "deleting area code: $areaCode")
        areaCodeDao.delete(areaCode)
    }

    companion object {
        private const val TAG = "AreaCodeRepository"
    }
}