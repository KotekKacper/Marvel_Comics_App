package com.kgkk.marvelcomicsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kgkk.marvelcomicsapp.ComicListAdapter
import com.kgkk.marvelcomicsapp.R
import com.kgkk.marvelcomicsapp.databinding.FragmentSearchBinding
import com.kgkk.marvelcomicsapp.utils.ComicSerialization
import com.kgkk.marvelcomicsapp.utils.Constants
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val comicViewModel = ViewModelProvider(this)[ComicsViewModel::class.java]

        // Inflate the layout and initialize the RecyclerView and its adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ComicListAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        val serializer = ComicSerialization()
        // Passing data and redirection to details screen
        adapter.setListener(object : ComicListAdapter.Listener {
            override fun onClick(position: Int) {
                val comic = comicViewModel.comicsByTitle.value?.get(position)
                val bundle = Bundle().apply {
                    putByteArray(
                        Constants.COMIC_OBJ_KEY,
                        comic?.let { serializer.serializeComic(it) })
                }
                view?.findNavController()?.navigate(R.id.navigation_details, bundle)
            }
        })

        // Showing recycler view or message if list is empty
        comicViewModel.comicsByTitle.observe(viewLifecycleOwner) { comics ->
            if (comics.isNotEmpty()) {
                adapter.setData(comics)
                binding.iconTextView.visibility = View.GONE
            } else {
                adapter.setData(comics)
                binding.iconTextView.text =
                    getString(R.string.no_results_text, binding.searchView.query)
                binding.iconTextView.visibility = View.VISIBLE
            }
        }

        comicViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide the loading indicator based on the loading state
            binding.progressContainer.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Clear the screen
                binding.iconTextView.visibility = View.GONE
                // Perform the search
                comicViewModel.searchComicsByTitle(query)
                // Close the keyboard
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}