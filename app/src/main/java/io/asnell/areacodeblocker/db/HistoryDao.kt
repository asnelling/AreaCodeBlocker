package io.asnell.areacodeblocker.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: History)
}
