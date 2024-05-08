package com.inven.alumine.suggestion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inven.alumine.R
import com.inven.alumine.suggestion.listener.ItemClickListener

class SuggestionAdapter : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {
    private var suggestionArray: MutableList<String> = mutableListOf()
    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestionViewHolder {
        return SuggestionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.suggestion_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        suggestionArray[holder.adapterPosition].apply {
            holder.textView.text = suggestionArray[holder.adapterPosition].trim()
        }
    }

    override fun getItemCount(): Int {
        return suggestionArray.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateSuggestionList(suggestionList: String?) {
        if (suggestionList == null) {
            suggestionArray.clear()
        } else {
            loadData(suggestionList)
        }
        notifyDataSetChanged()
    }

    private fun loadData(data: String) {
        suggestionArray.clear()
        data.split(",").forEach {
            if (it.trim().isNotBlank()) {
                suggestionArray.add(it)
            }
        }
    }

    inner class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView by lazy { view.findViewById(R.id.suggest_text) }
        val pickButton: View by lazy { view.findViewById(R.id.pick_sg) }
        val list_item: View by lazy { view.findViewById(R.id.suggestion_list) }

        init {
            pickButton.setOnClickListener {
                itemClickListener?.onPickButtonClick(suggestionArray[adapterPosition])
            }
            list_item.setOnClickListener {
                itemClickListener?.onItemClick(suggestionArray[adapterPosition])
            }
        }

    }

}