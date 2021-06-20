package io.asnell.prefixscreener.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Prefix::class, History::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prefixDao(): PrefixDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prefix_screener.db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
