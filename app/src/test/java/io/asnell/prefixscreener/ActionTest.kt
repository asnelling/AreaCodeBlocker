package io.asnell.prefixscreener

import android.content.Context
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.asnell.prefixscreener.db.Action
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActionTest {
    private lateinit var icon: ImageView

    @Before
    fun makeIcon() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        icon = ImageView(context)
    }

    @Test
    fun testDisallow_callResponseBuilder() {
        val action = Action.DISALLOW
        val response = action.makeResponse()
        assertEquals(true, response.disallowCall)
        assertEquals(false, response.rejectCall)
        assertEquals(false, response.silenceCall)
        assertEquals(false, response.skipCallLog)
        assertEquals(false, response.skipNotification)
    }

    @Test
    fun testReject_callResponseBuilder() {
        val action = Action.REJECT
        val response = action.makeResponse()
        assertEquals(true, response.disallowCall)
        assertEquals(true, response.rejectCall)
        assertEquals(false, response.silenceCall)
        assertEquals(false, response.skipCallLog)
        assertEquals(false, response.skipNotification)
    }

    @Test
    fun testSilence_callResponseBuilder() {
        val action = Action.SILENCE
        val response = action.makeResponse()
        assertEquals(false, response.disallowCall)
        assertEquals(false, response.rejectCall)
        assertEquals(true, response.silenceCall)
        assertEquals(false, response.skipCallLog)
        assertEquals(false, response.skipNotification)
    }

    @Test
    fun testAllow_callResponseBuilder() {
        val action = Action.ALLOW
        val response = action.makeResponse()
        assertEquals(false, response.disallowCall)
        assertEquals(false, response.rejectCall)
        assertEquals(false, response.silenceCall)
        assertEquals(false, response.skipCallLog)
        assertEquals(false, response.skipNotification)
    }

    @Test
    fun testDisallow_setupIcon() {
        val action = Action.DISALLOW
        action.setupIcon(icon)
        assertEquals("Disallowed", icon.contentDescription)
    }

    @Test
    fun testReject_setupIcon() {
        val action = Action.REJECT
        action.setupIcon(icon)
        assertEquals("Rejected", icon.contentDescription)
    }

    @Test
    fun testSilence_setupIcon() {
        val action = Action.SILENCE
        action.setupIcon(icon)
        assertEquals("Silenced", icon.contentDescription)
    }

    @Test
    fun testAllow_setupIcon() {
        val action = Action.ALLOW
        action.setupIcon(icon)
        assertEquals("Allowed", icon.contentDescription)
    }
}