package com.kgkk.marvelcomicsapp.dataclasses

data class Comic(
    val id: Long,
    val title: String,
    val authors: ArrayList<Author>,
    val description: String,
    val imageUrl: String,
    val url: String
)
