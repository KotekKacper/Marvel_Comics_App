package com.kgkk.marvelcomicsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kgkk.marvelcomicsapp.ComicListAdapter
import com.kgkk.marvelcomicsapp.R
import com.kgkk.marvelcomicsapp.databinding.FragmentHomeBinding
import com.kgkk.marvelcomicsapp.utils.ComicSerialization
import com.kgkk.marvelcomicsapp.utils.Constants
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        // Inflate the layout and initialize the RecyclerView and it's adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ComicListAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        val serializer = ComicSerialization()
        // Passing data and redirection to details screen
        adapter.setListener(object : ComicListAdapter.Listener {
            override fun onClick(position: Int) {
                val comic = comicViewModel.comics.value?.get(position)
                val bundle = Bundle().apply {
                    putByteArray(
                        Constants.COMIC_OBJ_KEY,
                        comic?.let { serializer.serializeComic(it) })
                }
                view?.findNavController()?.navigate(R.id.navigation_details_home, bundle)
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