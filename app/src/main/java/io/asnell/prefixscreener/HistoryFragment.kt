package io.asnell.prefixscreener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val historyAdapter = HistoryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        view.findViewById<RecyclerView>(R.id.history_recycler).apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(view.context)
        }

        val emptyView = view.findViewById<View>(R.id.history_empty)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(STARTED) {
                historyViewModel.allHistory.collect { history ->
                    historyAdapter.submitList(history)
                    emptyView.visibility =
                        if (history.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        return view
    }
}