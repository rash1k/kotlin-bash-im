package ru.geekbrains.bashim.ui

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.source_item.view.*
import ru.geekbrains.bashim.R
import ru.geekbrains.bashim.data.QuoteOfSource


class SourceOfQuotesAdapter(var listOfQuotes: MutableList<QuoteOfSource>,
                            context: Context) :
        RecyclerView.Adapter<SourceOfQuotesAdapter.ViewHolder>() {


    private val mListeners: MutableList<SourceChangeListener> = mutableListOf()
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot = mLayoutInflater.inflate(R.layout.source_item, parent, false)
        return ViewHolder(viewRoot).listen { position, type ->
            changeSource(position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList: QuoteOfSource = listOfQuotes[position]
        holder.title.text = itemList.desc
    }

    override fun getItemCount(): Int {
        return if (listOfQuotes.size > 0) listOfQuotes.size else 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.text
    }

    fun addListener(listener: SourceChangeListener) {
        mListeners.add(listener)
    }


    private fun changeSource(position: Int) {
        mListeners.forEach { it.changeQuotes(position) }
    }

    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        this.itemView.setOnClickListener { v: View ->
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    operator fun get(position: Int): QuoteOfSource {
        return listOfQuotes[position]
    }
}


interface SourceChangeListener {
    fun changeQuotes(position: Int)
}