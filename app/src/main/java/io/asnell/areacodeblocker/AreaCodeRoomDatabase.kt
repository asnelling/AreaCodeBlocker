package io.asnell.areacodeblocker

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [AreaCode::class],
    version = 2,
    autoMigrations = [
        AutoMigration ( from = 1, to = 2)
    ],
)
abstract class AreaCodeRoomDatabase : RoomDatabase() {
    abstract fun areaCodeDao(): AreaCodeDao

    private class AreaCodeDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.areaCodeDao())
                }
            }
        }

        suspend fun populateDatabase(areaCodeDao: AreaCodeDao) {
            areaCodeDao.deleteAll()

            var areaCode = AreaCode("313")
            areaCodeDao.insert(areaCode)
            areaCode = AreaCode("484")
            areaCodeDao.insert(areaCode)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AreaCodeRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AreaCodeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AreaCodeRoomDatabase::class.java,
                    "area_code_database"
                )
                    .addCallback(AreaCodeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
