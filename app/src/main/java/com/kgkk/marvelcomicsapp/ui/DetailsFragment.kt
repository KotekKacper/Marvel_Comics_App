package com.kgkk.marvelcomicsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kgkk.marvelcomicsapp.databinding.FragmentDetailsBinding
import com.kgkk.marvelcomicsapp.models.Comic
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var comic: Comic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comic = deserializeComic(requireArguments().getByteArray("comic"))
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
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Handle back button click
        }

        setContent()

        return root
    }

    private fun setContent(){
        binding.comicTitle.text = comic?.title
        binding.comicAuthor.text = comic?.authors.toString()
        binding.comicDescription.text = comic?.description
    }

    private fun deserializeComic(byteArray: ByteArray?): Comic? {
        byteArray?.let {
            val inputStream = ByteArrayInputStream(it)
            val objectInputStream = ObjectInputStream(inputStream)
            val comic = objectInputStream.readObject() as? Comic
            objectInputStream.close()
            return comic
        }
        return null
    }
}