package io.asnell.prefixscreener

import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix

class MainActivity : AppCompatActivity() {
    private val prefixViewModel: PrefixViewModel by viewModels {
        PrefixViewModelFactory(
            (application as PrefixScreenerApplication).repository
        )
    }

    override fun onStart() {
        super.onStart()
        val roleManager = getSystemService(RoleManager::class.java)
        if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
            findViewById<View>(R.id.permission_notice).visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emptyView = findViewById<TextView>(R.id.empty_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = PrefixListAdapter()
        adapter.removeListener = PrefixListAdapter
            .RemovePrefixListener { prefix -> prefixViewModel.delete(prefix) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        prefixViewModel.allPrefixes.observe(this, { prefixes ->
            adapter.submitList(prefixes)
            emptyView.visibility =
                if (prefixes.isEmpty()) View.VISIBLE else View.GONE
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val fragment = NewPrefixDialogFragment()
            fragment.show(supportFragmentManager, NewPrefixDialogFragment.TAG)
        }

        supportFragmentManager.setFragmentResultListener(
            NewPrefixDialogFragment.TAG, this
        ) { _, bundle ->
            val prefixNums = bundle
                .getString("prefix") ?: return@setFragmentResultListener
            val action = bundle
                .getString("action") ?: return@setFragmentResultListener
            val prefix = Prefix(
                prefixNums,
                action = Action.valueOf(action)
            )
            prefixViewModel.insert(prefix)
        }

        val becomeCallScreener = registerForActivityResult(
            BecomeCallScreener()
        ) { result ->
            if (result) {
                findViewById<View>(R.id.permission_notice).visibility =
                    View.GONE
                Log.i(TAG, "set as default call screening app")
            } else {
                Log.w(TAG, "call screening NOT active")
            }
        }

        findViewById<Button>(R.id.set_default_screener).setOnClickListener {
            becomeCallScreener.launch(null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_help -> {
            val url = getString(R.string.help_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    this, R.string.no_browser_installed,
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return true
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}