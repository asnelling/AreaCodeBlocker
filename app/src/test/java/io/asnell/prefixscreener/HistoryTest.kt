package io.asnell.prefixscreener

import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.History
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoryTest {
    @Test
    fun test_properties() {
        val history = History(1625711245321, "+13334445555", Action.SILENCE, 0)
        assertEquals(1625711245321, history.receivedAt)
        assertEquals("+13334445555", history.callerNumber)
        assertEquals(Action.SILENCE, history.result)
        assertEquals(0, history.callerNumberVerificationStatus)
    }
}
