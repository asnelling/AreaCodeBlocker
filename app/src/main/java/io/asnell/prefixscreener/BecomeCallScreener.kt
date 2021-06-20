package io.asnell.prefixscreener

import android.app.Activity.RESULT_OK
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class BecomeCallScreener : ActivityResultContract<Unit, Boolean>() {

    override fun createIntent(context: Context, input: Unit?) = context
        .getSystemService(RoleManager::class.java)
        .createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        resultCode == RESULT_OK
}