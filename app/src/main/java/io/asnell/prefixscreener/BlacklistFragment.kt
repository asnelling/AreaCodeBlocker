package io.asnell.prefixscreener

import android.app.role.RoleManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.asnell.prefixscreener.db.Action
import io.asnell.prefixscreener.db.Prefix

class BlacklistFragment : Fragment() {
    private val prefixViewModel: PrefixViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blacklist, container, false)

        val roleManager = view.context.getSystemService(RoleManager::class.java)
        if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
            view.findViewById<View>(R.id.permission_notice).visibility = View.VISIBLE
        }

        val emptyView = view.findViewById<TextView>(R.id.empty_view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = PrefixListAdapter()
        adapter.removeListener = PrefixListAdapter
            .RemovePrefixListener { prefix -> prefixViewModel.delete(prefix) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        prefixViewModel.allPrefixes.observe(viewLifecycleOwner, { prefixes ->
            adapter.submitList(prefixes)
            emptyView.visibility =
                if (prefixes.isEmpty()) View.VISIBLE else View.GONE
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val fragment = NewPrefixDialogFragment()
            fragment.show(parentFragmentManager, NewPrefixDialogFragment.TAG)
        }

        parentFragmentManager.setFragmentResultListener(
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
                view.findViewById<View>(R.id.permission_notice).visibility =
                    View.GONE
            }
        }

        view.findViewById<Button>(R.id.set_default_screener).setOnClickListener {
            becomeCallScreener.launch(null)
        }

        return view
    }

    companion object {
    }
}