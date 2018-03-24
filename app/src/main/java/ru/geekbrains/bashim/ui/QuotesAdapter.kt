package ru.geekbrains.bashim.ui

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.source_item.view.*
import ru.geekbrains.bashim.R
import ru.geekbrains.bashim.data.Quote


class QuotesAdapter(var listOfQuotes: MutableList<Quote>,
                    context: Context) :
        RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot = mLayoutInflater.inflate(R.layout.quotes_item, parent, false)
        return ViewHolder(viewRoot).listen { position, type -> Unit }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList: Quote = listOfQuotes[position]
        holder.title.text = Html.fromHtml(itemList.htmlText)
    }

    override fun getItemCount(): Int {
        return if (listOfQuotes.size > 0) listOfQuotes.size else 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: AppCompatTextView = itemView.text!!
    }


    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        this.itemView.setOnClickListener { event.invoke(adapterPosition, itemViewType) }
        return this
    }

    operator fun get(position: Int): Quote {
        return listOfQuotes[position]
    }
}