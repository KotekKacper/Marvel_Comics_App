package com.kgkk.marvelcomicsapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val marvelApi: MarvelApi by lazy {
        RetrofitHelper.getInstance().create(MarvelApi::class.java)
    }
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    @OptIn(DelicateCoroutinesApi::class)
    val comics: MutableLiveData<List<Comic>> by lazy {
        val comicsLiveData = MutableLiveData<List<Comic>>()
        _loadingState.value = true

        // get comic list in the background
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = marvelApi.getComics()
                val comics = convertResponseToModel(result)
                comicsLiveData.postValue(comics)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loadingState.postValue(false)
            }
        }

        comicsLiveData
    }

    val comicsByTitle = MutableLiveData<List<Comic>>()

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
                    && result.images.isNotEmpty()) {
                    "${result.images[0].path}.${result.images[0].extension}"
                } else {
                    "${result.thumbnail.path}.${result.thumbnail.extension}"
                }

            Comic(
                result.id,
                result.title,
                authors,
                result.textObjects.getOrNull(0)?.text,
                chosenImageUrl,
                result.urls[0].url
            )
        } ?: emptyList()
    }
}