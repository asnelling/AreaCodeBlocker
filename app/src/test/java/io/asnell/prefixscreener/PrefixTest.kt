package io.asnell.prefixscreener

import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix
import junit.framework.Assert.assertEquals
import org.junit.Test

class PrefixTest {
    @Test fun test_default_values() {
        val prefix = Prefix("+1214")
        assertEquals(null, prefix.id)
        assertEquals(Action.DISALLOW, prefix.action)
    }

    @Test fun test_set_values() {
        val prefix = Prefix("+1214", action = Action.SILENCE)
        assertEquals("+1214", prefix.number)
        assertEquals(Action.SILENCE, prefix.action)
    }
}