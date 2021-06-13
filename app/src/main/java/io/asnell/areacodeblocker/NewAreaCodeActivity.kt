package io.asnell.areacodeblocker

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class NewAreaCodeActivity : AppCompatActivity() {
    private lateinit var editAreaCodeView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_area_code)

        editAreaCodeView = findViewById(R.id.edit_area_code)
        val actionsDropdownView = findViewById<MaterialAutoCompleteTextView>(R.id.actions_dropdown)
        actionsDropdownView.setAdapter(ArrayAdapter.createFromResource(this, R.array.call_actions, android.R.layout.simple_dropdown_item_1line))

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editAreaCodeView.text)) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                val areaCode = editAreaCodeView.text.toString()
                val action = actionsDropdownView.text.toString().uppercase()
                replyIntent.putExtra(EXTRA_REPLY, areaCode)
                replyIntent.putExtra(EXTRA_ACTION, action)
                setResult(RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "io.asnell.areacodeblocker.REPLY"
        const val EXTRA_ACTION = "io.asnell.areacodeblocker.ACTION"
    }
}