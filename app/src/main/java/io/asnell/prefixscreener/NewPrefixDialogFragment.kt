package io.asnell.prefixscreener

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.asnell.prefixscreener.db.Action

class NewPrefixDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_new_prefix_dialog,
            container,
            false
        )

        val editPrefixView: EditText = view.findViewById(R.id.edit_prefix)
        view.findViewById<Button>(R.id.button_block)
            .setOnClickListener {
                if (TextUtils.isEmpty(editPrefixView.text)) {
                    // handle validation error
                } else {
                    val prefix = editPrefixView.text.toString()
                    val action = Action.DISALLOW.name
                    val result = bundleOf(
                        "prefix" to "+$prefix",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult(
                        "new_prefix",
                        result
                    )
                    dismiss()
                }
            }

        view.findViewById<Button>(R.id.button_reject)
            .setOnClickListener {
                if (TextUtils.isEmpty(editPrefixView.text)) {
                    // handle validation error
                } else {
                    val prefix = editPrefixView.text.toString()
                    val action = Action.REJECT.name
                    val result = bundleOf(
                        "prefix" to "+$prefix",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult(
                        "new_prefix",
                        result
                    )
                    dismiss()
                }
            }

        view.findViewById<Button>(R.id.button_silence)
            .setOnClickListener {
                if (TextUtils.isEmpty(editPrefixView.text)) {
                    // handle validation error
                } else {
                    val prefix = editPrefixView.text.toString()
                    val action = Action.SILENCE.name
                    val result = bundleOf(
                        "prefix" to "+$prefix",
                        "action" to action,
                    )
                    parentFragmentManager.setFragmentResult(
                        "new_prefix",
                        result
                    )
                    dismiss()
                }
            }

        return view
    }
}