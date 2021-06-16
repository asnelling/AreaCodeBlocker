package io.asnell.areacodeblocker

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class NewAreaCodeDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_new_area_code_dialog,
            container,
            false
        )

        val actionsDropdownView: MaterialAutoCompleteTextView = view
            .findViewById(R.id.actions_dropdown)
        actionsDropdownView.setAdapter(ArrayAdapter.createFromResource(
            view.context,
            R.array.call_actions,
            android.R.layout.simple_dropdown_item_1line))

        val editAreaCodeView: EditText = view.findViewById(R.id.edit_area_code)
        view.findViewById<Button>(R.id.button_save)
            .setOnClickListener {
                val replyIntent = Intent()
                if (TextUtils.isEmpty(editAreaCodeView.text)) {
                    // handle validation error
                } else {
                    val areaCode = editAreaCodeView.text.toString()
                    val action = actionsDropdownView.text.toString().uppercase()
                    val result = bundleOf(
                        "areaCode" to areaCode,
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult("new_area_code",
                        result)
                }
            }


        return view
    }

    companion object {
        const val EXTRA_REPLY = "io.asnell.areacodeblocker.REPLY"
        const val EXTRA_ACTION = "io.asnell.areacodeblocker.ACTION"
    }
}