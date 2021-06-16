package io.asnell.areacodeblocker

import android.util.Log
import androidx.annotation.WorkerThread
import io.asnell.areacodeblocker.db.AreaCode
import io.asnell.areacodeblocker.db.AreaCodeRoomDatabase
import io.asnell.areacodeblocker.db.History
import kotlinx.coroutines.flow.Flow

class Repository(private val database: AreaCodeRoomDatabase) {
    val allAreaCodes: Flow<List<AreaCode>> = database.areaCodeDao().getAreaCodes()

    @WorkerThread
    suspend fun insert(areaCode: AreaCode) {
        database.areaCodeDao().insert(areaCode)
    }

    @WorkerThread
    suspend fun delete(areaCode: AreaCode) {
        database.areaCodeDao().delete(areaCode)
    }

    @WorkerThread
    suspend fun insert(history: History) {
        database.historyDao().insert(history)
    }
}