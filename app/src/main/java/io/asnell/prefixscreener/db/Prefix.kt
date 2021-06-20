package io.asnell.prefixscreener.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Prefix(
    val number: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val action: Action? = Action.DISALLOW,
)

enum class Action {
    DISALLOW, REJECT, SILENCE
}
