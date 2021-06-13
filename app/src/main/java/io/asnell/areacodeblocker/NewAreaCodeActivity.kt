package io.asnell.areacodeblocker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewAreaCodeActivity : AppCompatActivity() {
    private lateinit var editAreaCodeView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_area_code)

        editAreaCodeView = findViewById(R.id.edit_area_code)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editAreaCodeView.text)) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                val areaCode = editAreaCodeView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, areaCode)
                setResult(RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "io.asnell.areacodeblocker.REPLY"
    }
}