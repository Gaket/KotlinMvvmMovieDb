package ru.gaket.themoviedb

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import ru.gaket.themoviedb.di.AppComponent

class MovieApp : Application() {

  val myComponent: AppComponent by lazy {
    AppComponent(this)
  }

  override fun onCreate() {
    super.onCreate()
  }
}
