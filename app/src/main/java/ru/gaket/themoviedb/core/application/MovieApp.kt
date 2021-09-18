package ru.gaket.themoviedb.core.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.gaket.themoviedb.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class MovieApp : Application() {
	
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}
}