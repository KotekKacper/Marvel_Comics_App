package com.kgkk.marvelcomicsapp.models

import java.io.Serializable

data class Comic(
    val id: Long,
    val title: String,
    val authors: List<Author>,
    val description: String?,
    val imageUrl: String?,
    val url: String
) : Serializable {
    constructor() : this(0, "", emptyList(), null, null, "")
}

