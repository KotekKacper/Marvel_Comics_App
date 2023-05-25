package com.kgkk.marvelcomicsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kgkk.marvelcomicsapp.api.ApiResponse
import com.kgkk.marvelcomicsapp.api.MarvelApi
import com.kgkk.marvelcomicsapp.api.RetrofitHelper
import com.kgkk.marvelcomicsapp.models.Author
import com.kgkk.marvelcomicsapp.models.Comic
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ComicsViewModel: ViewModel() {
    private val marvelApi: MarvelApi by lazy {
        RetrofitHelper.getInstance().create(MarvelApi::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    val comics: MutableLiveData<List<Comic>> by lazy {
        val comicsLiveData = MutableLiveData<List<Comic>>()

        // get comic list in the background
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = marvelApi.getComics()
                val comics = convertResponseToModel(result)
                comicsLiveData.postValue(comics)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        comicsLiveData
    }

    @OptIn(DelicateCoroutinesApi::class)
    val comicsByTitle: MutableLiveData<List<Comic>> by lazy {
        val comicsLiveData = MutableLiveData<List<Comic>>()

        // get comic list in the background
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = marvelApi.getComicsByTitle(title = "america")
                val comics = convertResponseToModel(result)
                comicsLiveData.postValue(comics)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        comicsLiveData
    }

    private fun convertResponseToModel(response: Response<ApiResponse>): List<Comic> {
        return response.body()?.data?.results?.map { result ->
            val authors = result.creators.items.map { item ->
                Author(item.role, item.name)
            }
            Comic(
                result.id,
                result.title,
                authors,
                result.textObjects.getOrNull(0)?.text,
                "${result.thumbnail.path}.${result.thumbnail.extension}",
                result.urls[0].url
            )
        } ?: emptyList()
    }
}