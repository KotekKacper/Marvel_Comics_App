package com.kgkk.marvelcomicsapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kgkk.marvelcomicsapp.api.ApiResponse
import com.kgkk.marvelcomicsapp.api.MarvelApi
import com.kgkk.marvelcomicsapp.api.RetrofitHelper
import com.kgkk.marvelcomicsapp.models.Author
import com.kgkk.marvelcomicsapp.models.Comic
import com.kgkk.marvelcomicsapp.utils.Constants
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ComicsViewModel : ViewModel() {
    val currentUser: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>().apply {
            value = FirebaseAuth.getInstance().currentUser
        }
    }

    private val db = FirebaseFirestore.getInstance()
    val comicsCollection = db.collection("comics")

    private val marvelApi: MarvelApi by lazy {
        RetrofitHelper.getInstance().create(MarvelApi::class.java)
    }

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    val comics: MutableLiveData<List<Comic>> by lazy {
        val comicsLiveData = MutableLiveData<List<Comic>>()
        _loadingState.value = true
        loadComics(marvelApi, _loadingState, comicsLiveData)
        comicsLiveData
    }
    val comicsByTitle = MutableLiveData<List<Comic>>()
    val comicsSaved = MutableLiveData<List<Comic>>()

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadComics(
        marvelApi: MarvelApi,
        loadingState: MutableLiveData<Boolean>,
        comicsLiveData: MutableLiveData<List<Comic>>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = marvelApi.getComics()
                val comics = convertResponseToModel(result)
                comicsLiveData.postValue(comics)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loadingState.postValue(false)
            }
        }
    }

    fun runIfComicSaved(comicId: Long, negativeLambdaFunction: () -> Unit, lambdaFunction: () -> Unit) {
        val query = comicsCollection
            .whereEqualTo("id", comicId)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Comic with the given ID and user ID does exist
                    lambdaFunction()
                } else {
                    negativeLambdaFunction()
                }
            }
            .addOnFailureListener { exception ->
                Log.i("Firebase", exception.toString())
            }
    }


    fun addComic(comic: Comic) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val query = comicsCollection
            .whereEqualTo("id", comic.id)
            .whereEqualTo("userId", userId)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Comic with the given ID and user ID exists
                    Log.d("Firebase", "Comic already saved")
                } else {
                    // Comic with the given ID and user ID does not exist
                    val comicData = mapOf(
                        "id" to comic.id,
                        "title" to comic.title,
                        "authors" to comic.authors.map { mapOf("role" to it.role, "name" to it.name) },
                        "description" to comic.description,
                        "imageUrl" to comic.imageUrl,
                        "url" to comic.url,
                        "userId" to userId
                    )

                    comicsCollection.add(comicData)
                        .addOnSuccessListener { documentReference ->
                            // Comic added successfully
                            val comicId = documentReference.id
                            // Perform any additional actions or UI updates
                            Log.i("Firebase", "$comicId added successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.i("Firebase", exception.toString())
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.i("Firebase", exception.toString())
            }
    }

    fun removeComic(comicId: Long) {
        val query = comicsCollection
            .whereEqualTo("id", comicId)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {
                    documentSnapshot.reference.delete()
                        .addOnSuccessListener {
                            // Comics removed successfully
                            Log.i("Firebase", "$comicId removed successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.i("Firebase", exception.toString())
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.i("Firebase", exception.toString())
            }
    }


    fun getUserComics() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val comicsCollection = FirebaseFirestore.getInstance().collection("comics")
            val query = comicsCollection.whereEqualTo("userId", userId)

            query.get()
                .addOnSuccessListener { querySnapshot ->
                    val comics = mutableListOf<Comic>()

                    for (documentSnapshot in querySnapshot.documents) {
                        val comic = documentSnapshot.toObject(Comic::class.java)
                        comic?.let { comics.add(it) }
                    }

                    comicsSaved.postValue(comics)
                    Log.d("Firebase", "Comics saved")
                }
                .addOnFailureListener { exception ->
                    comicsSaved.postValue(emptyList())
                    Log.d("Firebase", exception.toString())
                }
        } else {
            // User is not authenticated
            comicsSaved.postValue(emptyList())
            Log.d("Firebase", "User is not authenticated")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun searchComicsByTitle(title: String) {
        _loadingState.value = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = marvelApi.getComicsByTitle(title)
                val comics = convertResponseToModel(result)
                comicsByTitle.postValue(comics)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loadingState.postValue(false)
            }
        }
    }

    private fun convertResponseToModel(response: Response<ApiResponse>): List<Comic> {
        return response.body()?.data?.results?.map { result ->
            val authors = result.creators.items.map { item ->
                Author(item.role, item.name)
            }
            val chosenImageUrl =
                if (result.thumbnail.path == Constants.IMG_NOT_AVAILABLE_URL
                    && result.images.isNotEmpty()
                ) {
                    "${result.images[0].path}.${result.images[0].extension}"
                } else {
                    "${result.thumbnail.path}.${result.thumbnail.extension}"
                }

            Comic(
                result.id,
                result.title,
                authors,
                result.textObjects.getOrNull(0)?.text?.let { removeHtmlTagsAndFixLineBreaks(it) },
                chosenImageUrl,
                result.urls[0].url
            )
        } ?: emptyList()
    }

    private fun removeHtmlTagsAndFixLineBreaks(htmlString: String): String {
        // Replace <br> tags with line breaks
        return htmlString.replace("\n", "").replace("<br>", "\n")
    }
}