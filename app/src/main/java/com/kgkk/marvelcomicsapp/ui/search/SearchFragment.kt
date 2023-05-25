package com.kgkk.marvelcomicsapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kgkk.marvelcomicsapp.ComicListAdapter
import com.kgkk.marvelcomicsapp.databinding.FragmentSearchBinding
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

        comicViewModel.comicsByTitle.observe(viewLifecycleOwner) { comics ->
            adapter.setData(comics)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Perform the search
                comicViewModel.searchComicsByTitle(query)
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