package com.kgkk.marvelcomicsapp.api

import com.kgkk.marvelcomicsapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper : IApiComicHelper {

    private val baseUrl = Constants.BASE_GATEWAY_URL

    override fun getApi(): MarvelApi {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }
}