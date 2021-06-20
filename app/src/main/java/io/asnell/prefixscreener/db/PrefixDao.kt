package io.asnell.prefixscreener.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrefixDao {
    @Query("SELECT * FROM Prefix")
    fun getPrefixes(): Flow<List<Prefix>>

    @Insert
    suspend fun insert(prefix: Prefix)

    @Delete
    suspend fun delete(prefix: Prefix)
}