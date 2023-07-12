package com.kgkk.marvelcomicsapp.utils

import com.kgkk.marvelcomicsapp.BuildConfig

object Constants {
//    Api
    const val TIMESTAMP = "1"
    const val APIKEY = BuildConfig.MarvelApiKey
    const val HASH = BuildConfig.MarvelHash
    const val LIMIT = 25
    const val OFFSET = 0
    const val ORDERBY = "-onsaleDate"
    const val BASE_GATEWAY_URL = "https://gateway.marvel.com/"
    const val IMG_NOT_AVAILABLE_URL = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available"
//    Fragment's communication
    const val COMIC_OBJ_KEY = "comic"
//    Author categories
    val authorRoles = listOf("writer", "penciller", "cover artist")
}