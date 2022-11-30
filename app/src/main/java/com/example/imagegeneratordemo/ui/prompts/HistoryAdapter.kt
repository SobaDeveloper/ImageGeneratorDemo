package com.example.imagegeneratordemo.ui.prompts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imagegeneratordemo.databinding.RowPromptBinding
import com.example.imagegeneratordemo.model.Prompt
import com.example.imagegeneratordemo.utils.ImageExtensions.base64ToBitmap

class HistoryAdapter(private val listener: Listener? = null) : ListAdapter<Prompt,
        HistoryAdapter.PromptViewHolder>(PromptsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder =
        PromptViewHolder.from(listener, parent)

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PromptViewHolder(
        private val listener: Listener?,
        private val binding: RowPromptBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(prompt: Prompt) {
            itemView.setOnClickListener { listener?.onPromptSelected(prompt) }
            binding.ivImage.load(prompt.images.first().base64ToBitmap())
            binding.tvPrompt.text = prompt.prompt
        }

        companion object {
            fun from(listener: Listener?, parent: ViewGroup): PromptViewHolder {
                val binding = RowPromptBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return PromptViewHolder(listener, binding)
            }
        }
    }

    class PromptsDiffUtil : DiffUtil.ItemCallback<Prompt>() {
        override fun areItemsTheSame(oldItem: Prompt, newItem: Prompt): Boolean =
            oldItem.prompt == newItem.prompt

        override fun areContentsTheSame(oldItem: Prompt, newItem: Prompt): Boolean =
            oldItem == newItem
    }

    interface Listener {
        fun onPromptSelected(prompt: Prompt)
    }
}