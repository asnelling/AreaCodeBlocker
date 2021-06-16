package io.asnell.areacodeblocker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    val receivedAt: Long,
    val callerNumber: String,
    val result: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
