package com.kgkk.marvelcomicsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kgkk.marvelcomicsapp.ComicListAdapter
import com.kgkk.marvelcomicsapp.R
import com.kgkk.marvelcomicsapp.databinding.FragmentHomeBinding
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var comicViewModel: ComicsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        comicViewModel = ViewModelProvider(this)[ComicsViewModel::class.java]

        // Inflate the layout and initialize the RecyclerView and its adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ComicListAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        // Setup for details screen
        adapter.setListener(object : ComicListAdapter.Listener {
            override fun onClick(position: Int) {
                view?.findNavController()?.navigate(R.id.navigation_details, bundleOf(
                    "position" to position, "screen" to "home"
                ))
            }
        })

        comicViewModel.comics.observe(viewLifecycleOwner) { comics ->
            adapter.setData(comics)
        }

        comicViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide the loading indicator based on the loading state
            binding.progressContainer.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}