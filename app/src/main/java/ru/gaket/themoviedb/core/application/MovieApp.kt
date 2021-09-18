package ru.gaket.themoviedb.ru.gaket.themoviedb.core.application

import android.app.Application
import ru.gaket.themoviedb.di.AppComponent

class MovieApp : Application() {
	val myComponent: AppComponent by lazy { AppComponent(this) }
}