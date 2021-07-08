package io.asnell.prefixscreener

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BecomeCallScreenerTest {
    @Test
    fun testCreateIntent() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = BecomeCallScreener().createIntent(context, null)
        assertEquals("android.app.role.action.REQUEST_ROLE", intent.action)
    }

    @Test
    fun parseResult_okResult_returnsTrue() {
        val bcs = BecomeCallScreener()
        assert(bcs.parseResult(Activity.RESULT_OK, null))
    }

    @Test
    fun parseResult_nonOkResult_returnsFalse() {
        val bcs = BecomeCallScreener()
        Assert.assertFalse(bcs.parseResult(Activity.RESULT_CANCELED, null))
        Assert.assertFalse(bcs.parseResult(2, null))
        Assert.assertFalse(bcs.parseResult(3, null))
    }
}