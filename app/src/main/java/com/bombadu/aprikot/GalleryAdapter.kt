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

class GalleryAdapter internal  constructor(private val listData: List<ListItem>, c: Context):
RecyclerView.Adapter<GalleryAdapter.RecyclerHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(c)
    private var itemClickCallback: ItemClickCallback? = null

    internal interface ItemClickCallback {
        fun onItemClick(p: Int)
    }

    internal fun setItemClickCallback(inItemClickCallback: ItemClickCallback) {
        this.itemClickCallback = inItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = inflater.inflate(R.layout.gallery_card, parent, false)
        return RecyclerHolder(view)

    }

    override fun getItemCount(): Int {
        return  listData.size

    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val item = listData[position]
        holder.captionTextView.text = item.galleryCaptionText
        holder.timeTextView.text = item.galleryTimeDateText
        holder.uploaderTextView.text = item.galleryUploaderText
        val uri = Uri.parse(item.galleryUrlText)
        holder.cardImageView.setImageResource(item.galleryImage)
        Picasso.get().load(uri).into(holder.cardImageView)
    }


    inner class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        val uploaderTextView: TextView = itemView.findViewById(R.id.gallery_uploadedby_text_view)
        val captionTextView: TextView = itemView.findViewById(R.id.gallery_caption_text_view)
        val timeTextView: TextView = itemView.findViewById(R.id.gallery_time_text_view)
        val cardImageView: ImageView = itemView.findViewById(R.id.gallery_card_image_view)
        private val cardContainer: View = itemView.findViewById(R.id.gallery_card_container)

        /*init {
            cardContainer.setOnClickListener(this)
        }*/

        override fun onClick(v: View?) {
            itemClickCallback!!.onItemClick(adapterPosition)
        }

    }
}