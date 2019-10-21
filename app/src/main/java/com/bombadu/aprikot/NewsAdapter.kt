package com.bombadu.aprikot

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso

class NewsAdapter internal constructor(private val listData: List<ListItem>, c: Context) :
    RecyclerView.Adapter<NewsAdapter.RecyclerHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(c)
    private var itemClickCallback: ItemClickCallback? = null

    internal interface ItemClickCallback {
        fun onItemClick(p: Int)
    }

    internal fun setItemClickCallback(inItemClickCallback: ItemClickCallback) {
        this.itemClickCallback = inItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = inflater.inflate(R.layout.news_card_layout, parent, false)
        return RecyclerHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val item = listData[position]
        holder.authorTextView.text = item.authorText
        holder.titleTexView.text = item.titleText
        holder.descriptionTextView.text = item.descriptionText
        val uri = Uri.parse(item.imageUrlText)
        holder.cardImageView.setImageResource(item.imageImage)
        Picasso.get().load(uri).into(holder.cardImageView)

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val authorTextView: TextView = itemView.findViewById(R.id.byLine_text_view)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
        val titleTexView: TextView = itemView.findViewById(R.id.title_text_view)
        val cardImageView: ImageView = itemView.findViewById(R.id.news_card_image_view)
        private val cardContainer: View = itemView.findViewById(R.id.news_card_container)


        init {

            cardContainer.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            itemClickCallback!!.onItemClick(adapterPosition)
        }
    }

}

