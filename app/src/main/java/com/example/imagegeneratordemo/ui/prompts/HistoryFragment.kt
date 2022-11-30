package com.example.imagegeneratordemo.ui.prompts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagegeneratordemo.R
import com.example.imagegeneratordemo.databinding.FragmentHistoryBinding
import com.example.imagegeneratordemo.model.Prompt
import com.example.imagegeneratordemo.ui.dialogs.ImageViewerDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(), HistoryAdapter.Listener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val promptsAdapter = HistoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPrompts()
        initUi()
        setLiveData()
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUi() {
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_clear -> {
                    viewModel.clearPrompts()
                    promptsAdapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }

        binding.recyclerView.apply {
            adapter = promptsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deletePrompt(position)
                promptsAdapter.notifyItemRemoved(position)
                Snackbar.make(
                    binding.recyclerView,
                    getString(R.string.prompt_deleted),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun setLiveData() {
        viewModel.promptsLiveData.observe(viewLifecycleOwner) {
            promptsAdapter.submitList(it)
        }
    }

    override fun onPromptSelected(prompt: Prompt) =
        ImageViewerDialogFragment.newInstance(prompt.images[0], prompt.prompt)
            .show(childFragmentManager, "")
}