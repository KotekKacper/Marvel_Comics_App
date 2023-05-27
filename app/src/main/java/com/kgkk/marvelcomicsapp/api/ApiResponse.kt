package com.kgkk.marvelcomicsapp.api

data class ApiResponse(val code: Int,
                       val status: String,
                       val data: MarvelData
)

data class MarvelData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<ComicResults>
)

data class ComicResults(
    val id: Long,
    val title: String,
    val urls: List<Url>,
    val thumbnail: Thumbnail,
    val images: List<Thumbnail>,
    val creators: Creators,
    val textObjects: List<Description>
)

data class Url(
    val type: String,
    val url: String
)

data class Thumbnail(
    val path: String,
    val extension: String
)

data class Creators(
    val items: List<Creator>
)

data class Creator(
    val name: String,
    val role: String
)

data class Description(
    val text: String
)
