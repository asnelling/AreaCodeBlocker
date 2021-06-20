package io.asnell.prefixscreener

import android.util.Log

fun debug(tag: String, msg: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, msg)
    }
}
