package com.kgkk.marvelcomicsapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kgkk.marvelcomicsapp.models.Author
import com.kgkk.marvelcomicsapp.models.Comic

class ComicListAdapter(
    private var comics: List<Comic>
) : RecyclerView.Adapter<ComicListAdapter.ViewHolder>() {

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

    class ViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
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
        val titleTextView = cardView.findViewById<TextView>(R.id.comic_title)
        val authorTextView = cardView.findViewById<TextView>(R.id.comic_author)
        val descTextView = cardView.findViewById<TextView>(R.id.comic_short_desc)

        Log.d("ImageUrl", comic.imageUrl.toString())
        if (comic.imageUrl != null) {
            Glide.with(cardView.context).load(comic.imageUrl).into(imageView)
        }

        titleTextView.text = comic.title
        authorTextView.text = getWriterText(comic.authors)
        descTextView.text = comic.description

        cardView.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    private fun getWriterText(authors: List<Author>): String {
        val writers = ArrayList<String>()
        for (author in authors) {
            val role = author.role
            val name = author.name
            if (role.lowercase() == "writer") {
                writers.add(name)
            }
        }
        return if (writers.size == 0) {
            ""
        } else {
            "written by " + writers.joinToString(", ")
        }
    }

}