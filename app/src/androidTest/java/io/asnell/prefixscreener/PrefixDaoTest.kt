package io.asnell.prefixscreener

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.AppDatabase
import io.asnell.prefixscreener.db.Prefix
import io.asnell.prefixscreener.db.PrefixDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrefixDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var prefixDao: PrefixDao
    private val prefixA = Prefix("+1123", 1, Action.DISALLOW)
    private val prefixB = Prefix("+1234", 2, Action.SILENCE)
    private val prefixC = Prefix("+1345", 3, Action.REJECT)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        prefixDao = database.prefixDao()

        prefixDao.insert(prefixA)
        prefixDao.insert(prefixB)
        prefixDao.insert(prefixC)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetPrefixes() = runBlocking {
        val prefixes = prefixDao.getPrefixes().first()
        assertEquals(3, prefixes.size)

        assertEquals(prefixA, prefixes[0])
        assertEquals(prefixB, prefixes[1])
        assertEquals(prefixC, prefixes[2])
    }

    @Test
    fun testDelete() = runBlocking {
        prefixDao.delete(prefixC)

        val prefixes = prefixDao.getPrefixes().first()
        assertEquals(2, prefixes.size)

        assertEquals(prefixA, prefixes[0])
        assertEquals(prefixB, prefixes[1])
    }

    @Test
    fun testInsert() = runBlocking {
        prefixDao.insert(Prefix("+1456"))
        val size = prefixDao.getPrefixes().first().size
        assertEquals(4, size)
    }

    @Test
    fun testInsert_generatesUniqueId() = runBlocking {
        prefixDao.insert(Prefix("+1567"))
        prefixDao.insert(Prefix("+1678"))
        prefixDao.insert(Prefix("+1789"))
        val ids = prefixDao.getPrefixes().first().map { prefix -> prefix.id }
        val uniqueIds = ids.toSet()
        assert(ids.size == uniqueIds.size)
    }
}