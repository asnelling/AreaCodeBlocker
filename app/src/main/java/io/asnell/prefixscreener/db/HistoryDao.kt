package io.asnell.prefixscreener.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: History)

    @Query("SELECT * FROM History ORDER BY receivedAt DESC")
    fun getHistory(): Flow<List<History>>
}
