package io.asnell.prefixscreener

import android.app.role.RoleManager
import android.app.role.RoleManager.ROLE_CALL_SCREENING
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix

class MainActivity : AppCompatActivity() {
    private val prefixViewModel: PrefixViewModel by viewModels {
        PrefixViewModelFactory((application as PrefixScreenerApplication).repository)
    }

    override fun onStart() {
        super.onStart()
        val roleManager = getSystemService(RoleManager::class.java)
        if (!roleManager.isRoleHeld(ROLE_CALL_SCREENING)) {
            findViewById<View>(R.id.permission_notice).visibility = VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emptyView = findViewById<TextView>(R.id.empty_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PrefixListAdapter()
        adapter.removeListener = PrefixListAdapter.RemovePrefixListener { prefix ->
            Log.d(TAG, "removing prefix ${prefix.id}: ${prefix.number}")
            prefixViewModel.delete(prefix)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        prefixViewModel.allPrefixes.observe(this, { prefixes ->
            prefixes?.let { adapter.submitList(it) }
            if (prefixes.isEmpty()) {
                emptyView.visibility = VISIBLE
            } else {
                emptyView.visibility = GONE
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val fragment = NewPrefixDialogFragment()
            fragment.show(supportFragmentManager, NEW_PREFIX_FRAGMENT_TAG)
        }

        supportFragmentManager.setFragmentResultListener(
            NEW_PREFIX_REQUEST_KEY, this) { requestKey, bundle ->
            val prefixNums = bundle.getString("prefix")!!
            val action = bundle.getString("action")!!
            val prefix = Prefix(
                prefixNums,
                action = Action.valueOf(action))
            prefixViewModel.insert(prefix)
        }

        findViewById<Button>(R.id.set_default_screener).setOnClickListener {
            val roleManager = getSystemService(RoleManager::class.java)
            val intent = roleManager
                .createRequestRoleIntent(ROLE_CALL_SCREENING)
            startActivityForResult(intent, SELECT_SERVICE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_SERVICE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    findViewById<View>(R.id.permission_notice).visibility = GONE
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
        private const val SELECT_SERVICE_REQUEST_CODE = 2
        private const val NEW_PREFIX_REQUEST_KEY = "new_prefix"
        private const val NEW_PREFIX_FRAGMENT_TAG = "new_prefix"
        private const val TAG = "MainActivity"
    }
}