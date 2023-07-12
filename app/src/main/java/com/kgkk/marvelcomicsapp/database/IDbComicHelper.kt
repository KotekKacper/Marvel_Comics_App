package com.kgkk.marvelcomicsapp.database

import androidx.lifecycle.MutableLiveData
import com.kgkk.marvelcomicsapp.models.Comic

interface IDbComicHelper {

    fun addComic(comic: Comic)

    fun removeComic(comicId: Long)

    fun updateUserComics(comicsSaved: MutableLiveData<List<Comic>>)

    fun runIfComicSaved(comicId: Long, lambdaFunction: () -> Unit, negativeLambdaFunction: () -> Unit)
}