package com.kgkk.marvelcomicsapp.models

data class Comic(
    val id: Long,
    val title: String,
    val authors: List<Author>,
    val description: String?,
    val imageUrl: String?,
    val url: String
)
