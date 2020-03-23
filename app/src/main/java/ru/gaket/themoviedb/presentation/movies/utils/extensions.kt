package ru.gaket.themoviedb.presentation.movies.utils

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

val Int.toDp: Int
  get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()
