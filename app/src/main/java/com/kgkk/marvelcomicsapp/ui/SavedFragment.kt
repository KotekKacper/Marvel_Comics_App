package com.kgkk.marvelcomicsapp.ui

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.kgkk.marvelcomicsapp.ComicListAdapter
import com.kgkk.marvelcomicsapp.R
import com.kgkk.marvelcomicsapp.databinding.FragmentSavedBinding
import com.kgkk.marvelcomicsapp.utils.ComicSerialization
import com.kgkk.marvelcomicsapp.utils.Constants
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var comicViewModel: ComicsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        comicViewModel = ViewModelProvider(this)[ComicsViewModel::class.java]

        // Inflate the layout and initialize the RecyclerView and it's adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ComicListAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            onSignInResult(res)
        }

        binding.login.setOnClickListener {
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_MarvelComicsApp)
                .build()
            signInLauncher.launch(signInIntent)
        }

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
                view?.findNavController()?.navigate(R.id.navigation_details_saved, bundle)
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

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this.context, "Logged in", Toast.LENGTH_SHORT).show()
            binding.username.text = user?.email ?: getString(R.string.username_text)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Toast.makeText(this.context, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}