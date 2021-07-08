package io.asnell.prefixscreener

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.AppDatabase
import io.asnell.prefixscreener.db.History
import io.asnell.prefixscreener.db.Prefix
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: Repository

    @Before
    fun createRepository() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        repository = Repository(db)

        db.historyDao().insertAll(
            History(1625715555555, "+13334445555", Action.SILENCE, 0, 1),
            History(1625717777777, "+15557778888", Action.REJECT, 0, 2),
            History(1625713333333, "+12223334444", Action.DISALLOW, 1, 3),
        )
        db.prefixDao().insertAll(
            Prefix("+1555111", 1, Action.REJECT),
            Prefix("+1333", 2, Action.DISALLOW),
            Prefix("+17773", 3, Action.SILENCE),
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testAllHistory() = runBlocking {
        val count = repository.allHistory.first().size
        assertEquals(3, count)
    }

    @Test
    fun testAllPrefixes() = runBlocking {
        val count = repository.allPrefixes.first().size
        assertEquals(3, count)
    }

    @Test
    fun testMatchingPrefixes() = runBlocking {
        val count = repository.matchingPrefixes("+15551112222").first().size
        assertEquals(1, count)
    }

    @Test
    fun testInsertHistory() = runBlocking {
        repository.insert(
            History(
                1625714444444,
                "+17772223333",
                Action.REJECT,
                0
            )
        )
        val count = repository.allHistory.first().size
        assertEquals(4, count)
    }

    @Test
    fun testInsertPrefix() = runBlocking {
        repository.insert(Prefix("+1999"))
        val count = repository.allPrefixes.first().size
        assertEquals(4, count)
    }

    @Test
    fun testDeletePrefix() = runBlocking {
        repository.delete(Prefix("+17773", 3, Action.SILENCE))
        val count = repository.allPrefixes.first().size
        assertEquals(2, count)
    }
}