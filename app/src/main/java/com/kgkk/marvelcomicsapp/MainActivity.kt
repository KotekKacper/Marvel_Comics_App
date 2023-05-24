package com.kgkk.marvelcomicsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.kgkk.marvelcomicsapp.viewmodels.ComicsViewModel
class MainActivity : AppCompatActivity() {

    private lateinit var comicsViewModel: ComicsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        comicsViewModel = ViewModelProvider(this)[ComicsViewModel::class.java]

        val text = findViewById<TextView>(R.id.title_main)
        comicsViewModel.comics.observe(this) {
            text.text = comicsViewModel.comics.value?.get(0)?.title ?: "No data"
        }
    }
}