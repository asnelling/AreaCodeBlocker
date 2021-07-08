package io.asnell.prefixscreener

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.AppDatabase
import io.asnell.prefixscreener.db.Prefix
import io.asnell.prefixscreener.db.PrefixDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrefixDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var prefixDao: PrefixDao

    private val prefix1 = Prefix("+1555111", 1, Action.REJECT)
    private val prefix2 = Prefix("+1333", 2, Action.DISALLOW)
    private val prefix3 = Prefix("+17773", 3, Action.SILENCE)

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        prefixDao = db.prefixDao()

        prefixDao.insertAll(prefix1, prefix2, prefix3)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetPrefixes() = runBlocking {
        val prefixList = prefixDao.getPrefixes().first()
        assertEquals(3, prefixList.size)

        // prefixes should be sorted by number
        assertEquals(prefix2, prefixList[0])
        assertEquals(prefix1, prefixList[1])
        assertEquals(prefix3, prefixList[2])
    }

    @Test
    fun testGetMatching() = runBlocking {
        val prefixList = prefixDao.getMatching("+17773335555").first()
        assertEquals(1, prefixList.size)
        assertEquals(prefix3, prefixList[0])
    }

    @Test
    fun testDelete() = runBlocking {
        prefixDao.delete(prefix2)
        val prefixList = prefixDao.getPrefixes().first()
        assertEquals(2, prefixList.size)
        assertEquals(prefix1, prefixList[0])
        assertEquals(prefix3, prefixList[1])
    }

    @Test
    fun testInsert() = runBlocking {
        prefixDao.insert(Prefix("+188899", 4, Action.REJECT))
        val count = prefixDao.getPrefixes().first().size
        assertEquals(4, count)
    }
}