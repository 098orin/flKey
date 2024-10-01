package com.kazumaproject.markdownhelperkeyboard.ime_service.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.kazumaproject.markdownhelperkeyboard.R
import com.kazumaproject.markdownhelperkeyboard.converter.candidate.Candidate

class SuggestionAdapter : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {
    inner class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: MaterialTextView = itemView.findViewById(R.id.suggestion_item_text_view)
        val typeText: MaterialTextView = itemView.findViewById(R.id.suggestion_item_type_text_view)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Candidate>() {
        override fun areItemsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
            return oldItem.string == newItem.string
        }
    }

    private var onItemClickListener: ((Candidate) -> Unit)? = null

    fun setOnItemClickListener(onItemClick: (Candidate) -> Unit) {
        this.onItemClickListener = onItemClick
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var suggestions: List<Candidate>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item, parent, false)
        return SuggestionViewHolder(itemView)
    }

    override fun getItemCount(): Int = suggestions.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = suggestions[position]
        val paddingLength = if (position == 0) 4 else if (suggestion.length.toInt() >= 2) 0 else 1
        holder.text.text = suggestion.string.padStart(suggestion.string.length + paddingLength)
            .plus(" ".repeat(paddingLength))
        holder.typeText.text = when (suggestion.type) {
//            (1).toByte() -> "${suggestion.score}"
            (9).toByte() -> "[予測]"
            (5).toByte() -> "[部分]"
            (7).toByte() -> "[単]"
            (10).toByte() -> ""
            (11).toByte() -> "　　"
            (12).toByte() -> "　　"
            (13).toByte() -> "　　"
            (14).toByte() -> "[日付]"
            (15).toByte() -> "[修正]"
            else -> ""
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(suggestion)
        }
    }

}