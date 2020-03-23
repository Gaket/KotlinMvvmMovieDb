package ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies

import android.content.Context
import android.content.Intent
import android.net.Uri

class Navigator(private val context: Context) {

  fun navigateTo(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
  }
}