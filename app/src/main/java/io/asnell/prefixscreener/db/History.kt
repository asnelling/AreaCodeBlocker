package io.asnell.prefixscreener.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    val receivedAt: Long,
    val callerNumber: String,
    val result: String,

    @ColumnInfo(defaultValue = "0")
    val callerNumberVerificationStatus: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
