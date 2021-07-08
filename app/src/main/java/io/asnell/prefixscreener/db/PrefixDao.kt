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

    @Query("SELECT * FROM Prefix WHERE instr(:callerNumber, number) > 0 ORDER BY length(number) DESC")
    fun getMatching(callerNumber: String): Flow<List<Prefix>>

    @Insert
    suspend fun insert(prefix: Prefix)

    @Insert
    suspend fun insertAll(vararg prefixes: Prefix)

    @Delete
    suspend fun delete(prefix: Prefix)
}