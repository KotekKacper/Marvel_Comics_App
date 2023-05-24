package com.kgkk.marvelcomicsapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {
    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("ts") timestamp: String = Constants.TIMESTAMP,
        @Query("apikey") apiKey: String = Constants.APIKEY,
        @Query("hash") hash: String = Constants.HASH,
        @Query("limit") limit: Int = Constants.LIMIT,
        @Query("offset") offset: Int = Constants.OFFSET,
        @Query("orderBy") orderBy: String = Constants.ORDERBY
    ): Response<ApiResponse>
}
