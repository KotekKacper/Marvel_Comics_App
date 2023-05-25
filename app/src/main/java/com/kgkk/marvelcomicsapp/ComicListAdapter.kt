package com.kgkk.marvelcomicsapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kgkk.marvelcomicsapp.models.Comic

class ComicListAdapter (private var comics: List<Comic>
): RecyclerView.Adapter<ComicListAdapter.ViewHolder>() {

    private var listener: Listener? = null

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setData(comics: List<Comic>) {
        this.comics = comics
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView){
        var cardView: CardView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_comic, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView
        val comic = comics[position]

        val imageView = cardView.findViewById<ImageView>(R.id.comic_image)
        Log.d("ImageUrl", comic.imageUrl.toString())
        if (comic.imageUrl != null) {
            Glide.with(cardView.context)
                .load(comic.imageUrl)
                .into(imageView)
        }

        val titleTextView = cardView.findViewById<TextView>(R.id.comic_title)
        titleTextView.text = comic.title

        val authorTextView = cardView.findViewById<TextView>(R.id.comic_author)
        authorTextView.text = comic.authors.getOrNull(0)?.name ?: ""

        val descTextView = cardView.findViewById<TextView>(R.id.comic_short_desc)
        // Set the number of lines to display in the description
        imageView.post {
            val imageHeight: Int = imageView.height
            val combinedLineHeight: Int = titleTextView.lineHeight*titleTextView.lineCount + authorTextView.lineHeight*authorTextView.lineCount
            val maxLines: Int = (imageHeight - combinedLineHeight) / descTextView.lineHeight
            descTextView.maxLines = maxLines - 2
        }
        descTextView.text = comic.description

        cardView.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return comics.size
    }

}