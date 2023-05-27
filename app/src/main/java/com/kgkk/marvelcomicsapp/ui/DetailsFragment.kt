package com.kgkk.marvelcomicsapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kgkk.marvelcomicsapp.databinding.FragmentDetailsBinding
import com.kgkk.marvelcomicsapp.models.Author
import com.kgkk.marvelcomicsapp.models.Comic
import com.kgkk.marvelcomicsapp.utils.ComicSerialization
import com.kgkk.marvelcomicsapp.utils.Constants


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var comic: Comic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serializer = ComicSerialization()
        comic = serializer.deserializeComic(requireArguments().getByteArray("comic"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Show the back button
            setDisplayShowHomeEnabled(true) // Show the back button as an icon
            setDisplayShowTitleEnabled(false) // Don't show the title
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Handle back button click
        }

        // redirect to website
        binding.linkButton.setOnClickListener {
            val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(comic?.url))
            startActivity(viewIntent)
        }

        setContent()

        return root
    }

    private fun setContent(){
        binding.comicTitle.text = comic?.title
        binding.comicAuthor.text = getAuthorsSorted(comic)
        binding.comicDescription.text = comic?.description
        if (comic?.imageUrl != null) {
            Glide.with(binding.root.context)
                .load(comic?.imageUrl)
                .into(binding.comicImage)
        }
    }

    private fun getAuthorsSorted(comic: Comic?): String {
        val sortedAuthors = Constants.authorRoles
            .flatMap { role -> getAuthorsByRole(comic?.authors, role) }
        val allAuthorsWithDuplicates = (sortedAuthors + getAuthorsByRole(comic?.authors, ""))
            .toMutableList()
        val allAuthors = removeDuplicates(allAuthorsWithDuplicates)

        return if (allAuthors.isEmpty()) {
            ""
        } else {
            allAuthors.joinToString(", ")
        }
    }

    private fun getAuthorsByRole(authors: List<Author>?, roleSearch: String): List<String> {
        return authors
            ?.filter { author ->
                val role = author.role.lowercase()
                role.startsWith(roleSearch) || (roleSearch == "" && !Constants.authorRoles.contains(role))
            }
            ?.map { it.name }
            ?: emptyList()
    }

    fun <T> removeDuplicates(list: List<T>): List<T> {
        val set = LinkedHashSet<T>()
        val result = mutableListOf<T>()
        for (element in list) {
            if (set.add(element)) {
                result.add(element)
            }
        }
        return result
    }
}