package com.kgkk.marvelcomicsapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
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

        // When image loading is complete, update the description TextView height
        updateUIWhenImageLoaded(imageView) {
            val imageHeight: Int = imageView.height
            val combinedLineHeight: Int =
                titleTextView.lineHeight * titleTextView.lineCount + authorTextView.lineHeight * authorTextView.lineCount
            val maxLines: Int = (imageHeight - combinedLineHeight) / descTextView.lineHeight
            descTextView.maxLines = maxLines - 2
            try {
                descTextView.text = comics[holder.adapterPosition].description
            } catch (e: ArrayIndexOutOfBoundsException) {
                Log.d("Description height", "Couldn't update the height")
                Log.d("Error displayed", e.toString())
            }
        }

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

    private fun updateUIWhenImageLoaded(imageView: ImageView, callback: () -> Unit) {
        imageView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // Check if the image is loaded
                if (imageView.drawable != null) {
                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                    callback() // Invoke the callback function when the image is loaded
                }
                return true
            }
        })
    }

}