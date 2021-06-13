package io.asnell.areacodeblocker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
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
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        areaCodeViewModel.allAreaCodes.observe(this, Observer { areaCodes ->
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

        if (requestCode == NEW_AREA_CODE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getStringExtra(NewAreaCodeActivity.EXTRA_REPLY)?.let {
                val areaCode = AreaCode(it)
                areaCodeViewModel.insert(areaCode)
            }
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val NEW_AREA_CODE_REQUEST_CODE = 1
    }
}