package io.asnell.areacodeblocker

import android.app.role.RoleManager
import android.app.role.RoleManager.ROLE_CALL_SCREENING
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val areaCodeViewModel: AreaCodeViewModel by viewModels {
        AreaCodeViewModelFactory((application as AreaCodesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewAreaCodeActivity::class.java)
            startActivityForResult(intent, NEW_AREA_CODE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            NEW_AREA_CODE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val areaCodeNums = data?.getStringExtra(NewAreaCodeActivity.EXTRA_REPLY)!!
                    val action = data.getStringExtra(NewAreaCodeActivity.EXTRA_ACTION)!!
                    val areaCode = AreaCode(areaCodeNums, action = Action.valueOf(action))
                    areaCodeViewModel.insert(areaCode)
                } else {
                    Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
                }
            }
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
        private const val TAG = "MainActivity"
    }
}