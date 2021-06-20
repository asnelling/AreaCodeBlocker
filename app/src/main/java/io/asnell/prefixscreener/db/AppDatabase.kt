package io.asnell.prefixscreener.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Prefix::class, History::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prefixDao(): PrefixDao
    abstract fun historyDao(): HistoryDao

    private class AppDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.prefixDao())
                }
            }
        }

        suspend fun populateDatabase(prefixDao: PrefixDao) {
            prefixDao.deleteAll()

            var prefix = Prefix("313")
            prefixDao.insert(prefix)
            prefix = Prefix("484")
            prefixDao.insert(prefix)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prefix_screener.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
