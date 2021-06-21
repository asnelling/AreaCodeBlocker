package io.asnell.prefixscreener

import android.os.Bundle
import android.util.Log
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
                Log.d(TAG, "block button pressed")
                dismiss(editPrefixView.text, Action.DISALLOW)
            }

        view.findViewById<Button>(R.id.button_reject)
            .setOnClickListener {
                dismiss(editPrefixView.text, Action.REJECT)
            }

        view.findViewById<Button>(R.id.button_silence)
            .setOnClickListener {
                dismiss(editPrefixView.text, Action.SILENCE)
            }

        return view
    }

    fun dismiss(prefix: CharSequence, action: Action) {
        if (prefix.isBlank()) {
            return
        }

        val result = bundleOf(
            "prefix" to "+$prefix",
            "action" to action.name,
        )
        parentFragmentManager.setFragmentResult(TAG, result)
        dismiss()
    }

    companion object {
        const val TAG = "NewPrefixDialogFragment"
    }
}