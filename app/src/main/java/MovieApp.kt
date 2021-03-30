package ru.gaket.themoviedb

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import ru.gaket.themoviedb.di.AppComponent
import java.lang.IllegalArgumentException

class MovieApp : Application() {

  val myComponent: AppComponent by lazy {
    AppComponent(this)
  }

  override fun onCreate() {
    super.onCreate()
    getRemoteConfig()

    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
      if (!task.isSuccessful) {
        Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
        return@OnCompleteListener
      }

      // Get new FCM registration token
      val token = task.result

      // Log and toast
      val msg = "token is: $token"
      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    })
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
