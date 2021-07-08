package io.asnell.prefixscreener

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.AppDatabase
import io.asnell.prefixscreener.db.History
import io.asnell.prefixscreener.db.HistoryDao
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var historyDao: HistoryDao

    private val history1 = History(1625715555555, "+13334445555", Action.SILENCE, 0)
    private val history2 = History(1625717777777, "+15557778888", Action.REJECT, 0)
    private val history3 = History(1625713333333, "+12223334444", Action.DISALLOW, 1)

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        historyDao = db.historyDao()

        historyDao.insertAll(history1, history2, history3)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetHistory() = runBlocking {
        val historyList = historyDao.getHistory().first()
        assertEquals(3, historyList.size)

        // history should be sorted by time
        assertEquals(history3, historyList[0])
        assertEquals(history1, historyList[1])
        assertEquals(history2, historyList[2])
    }

    @Test
    fun testInsert() = runBlocking {
        historyDao.insert(History(1625722222222, "+19998887777", Action.REJECT, 0))
        val count = historyDao.getHistory().first().size
        assertEquals(4, count)
    }
}