package com.kgkk.marvelcomicsapp.utils

import com.kgkk.marvelcomicsapp.models.Comic
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ComicSerialization {

    fun serializeComic(comic: Comic): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(comic)
        objectOutputStream.close()
        return outputStream.toByteArray()
    }

    fun deserializeComic(byteArray: ByteArray?): Comic? {
        byteArray?.let {
            val inputStream = ByteArrayInputStream(it)
            val objectInputStream = ObjectInputStream(inputStream)
            val comic = objectInputStream.readObject() as? Comic
            objectInputStream.close()
            return comic
        }
        return null
    }
}