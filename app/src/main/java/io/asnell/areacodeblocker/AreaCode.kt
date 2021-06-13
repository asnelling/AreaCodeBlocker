package io.asnell.areacodeblocker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_code")
data class AreaCode(
    val code: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val action: Action? = Action.DISALLOW,
)

enum class Action {
    DISALLOW, REJECT, SILENCE
}
