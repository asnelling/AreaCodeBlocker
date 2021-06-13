package io.asnell.areacodeblocker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaCodeDao {
    @Query("SELECT * FROM area_code")
    fun getAreaCodes(): Flow<List<AreaCode>>

    @Insert
    suspend fun insert(areaCode: AreaCode)

    @Query("DELETE FROM area_code")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(areaCode: AreaCode)
}