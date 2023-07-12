package com.kgkk.marvelcomicsapp.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kgkk.marvelcomicsapp.models.Comic

class FirebaseDbComicHelper : IDbComicHelper {

    private val db = FirebaseFirestore.getInstance()
    private val comicsCollection = db.collection("comics")

    override fun addComic(comic: Comic) {
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

    override fun removeComic(comicId: Long) {
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

    override fun updateUserComics(comicsSaved: MutableLiveData<List<Comic>>) {
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

    override fun runIfComicSaved(comicId: Long, lambdaFunction: () -> Unit, negativeLambdaFunction: () -> Unit) {
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

}