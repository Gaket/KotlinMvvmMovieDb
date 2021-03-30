package ru.gaket.themoviedb

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import ru.gaket.themoviedb.di.AppComponent

class MovieApp : Application() {

  val myComponent: AppComponent by lazy {
    AppComponent(this)
  }

  override fun onCreate() {
    super.onCreate()
    getRemoteConfig()
  }

  private fun getRemoteConfig() {
    val remoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
      minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
    }

    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.setDefaultsAsync(FeatureToggles.defaults)
    remoteConfig.fetchAndActivate()
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val updated = task.result
//          Timber.d("Config params updated: $updated")
        } else {
//          Timber.w("Config params couldn't be updated")
        }
      }
  }
}
