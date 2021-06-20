package io.asnell.areacodeblocker

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.asnell.areacodeblocker.db.Action

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

        val editAreaCodeView: EditText = view.findViewById(R.id.edit_area_code)
        view.findViewById<Button>(R.id.button_block)
            .setOnClickListener {
                if (TextUtils.isEmpty(editAreaCodeView.text)) {
                    // handle validation error
                } else {
                    val areaCode = editAreaCodeView.text.toString()
                    val action = Action.DISALLOW.name
                    val result = bundleOf(
                        "areaCode" to "+$areaCode",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult("new_area_code",
                        result)
                    dismiss()
                }
            }

        view.findViewById<Button>(R.id.button_reject)
            .setOnClickListener {
                if (TextUtils.isEmpty(editAreaCodeView.text)) {
                    // handle validation error
                } else {
                    val areaCode = editAreaCodeView.text.toString()
                    val action = Action.REJECT.name
                    val result = bundleOf(
                        "areaCode" to "+$areaCode",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult("new_area_code",
                        result)
                    dismiss()
                }
            }

        view.findViewById<Button>(R.id.button_silence)
            .setOnClickListener {
                if (TextUtils.isEmpty(editAreaCodeView.text)) {
                    // handle validation error
                } else {
                    val areaCode = editAreaCodeView.text.toString()
                    val action = Action.SILENCE.name
                    val result = bundleOf(
                        "areaCode" to "+$areaCode",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult("new_area_code",
                        result)
                    dismiss()
                }
            }

        return view
    }
}