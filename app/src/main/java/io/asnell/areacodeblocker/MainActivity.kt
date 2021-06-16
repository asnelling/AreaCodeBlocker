package io.asnell.areacodeblocker

import android.app.role.RoleManager
import android.app.role.RoleManager.ROLE_CALL_SCREENING
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.asnell.areacodeblocker.db.Action
import io.asnell.areacodeblocker.db.AreaCode

class MainActivity : AppCompatActivity() {
    private val areaCodeViewModel: AreaCodeViewModel by viewModels {
        AreaCodeViewModelFactory((application as AreaCodesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emptyView = findViewById<TextView>(R.id.empty_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AreaCodeListAdapter()
        adapter.removeListener = AreaCodeListAdapter.AreaCodeRemoveListener { areaCode ->
            Log.d(TAG, "removing area code ${areaCode.id}: ${areaCode.code}")
            areaCodeViewModel.delete(areaCode)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        areaCodeViewModel.allAreaCodes.observe(this, { areaCodes ->
            areaCodes?.let { adapter.submitList(it) }
            if (areaCodes.isEmpty()) {
                emptyView.visibility = VISIBLE
            } else {
                emptyView.visibility = INVISIBLE
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val fragment = NewAreaCodeDialogFragment()
            fragment.show(supportFragmentManager, NEW_AREA_CODE_FRAGMENT_TAG)
        }

        supportFragmentManager.setFragmentResultListener(
            NEW_AREA_CODE_REQUEST_KEY, this) { requestKey, bundle ->
            val areaCodeNums = bundle.getString("areaCode")!!
            val action = bundle.getString("action")!!
            val areaCode = AreaCode(
                areaCodeNums,
                action = Action.valueOf(action))
            areaCodeViewModel.insert(areaCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_SERVICE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "call screening active")
                } else {
                    Log.d(TAG, "call screening NOT active")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_select_service -> {
            val roleManager = getSystemService(RoleManager::class.java)
            val intent = roleManager
                .createRequestRoleIntent(ROLE_CALL_SCREENING)
            startActivityForResult(intent, SELECT_SERVICE_REQUEST_CODE)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return true
    }

    companion object {
        private const val NEW_AREA_CODE_REQUEST_CODE = 1
        private const val SELECT_SERVICE_REQUEST_CODE = 2
        private const val NEW_AREA_CODE_REQUEST_KEY = "new_area_code"
        private const val NEW_AREA_CODE_FRAGMENT_TAG = "new_area_code"
        private const val TAG = "MainActivity"
    }
}