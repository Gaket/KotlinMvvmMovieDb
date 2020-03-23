package ru.gaket.themoviedb.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gaket.themoviedb.model.movies.network.MoviesApi
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModel
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.Navigator

/**
 * Class creating dependencies on the App level
 */
class AppComponent(appContext: Context) {

  private val moviesRepo: MoviesRepository
  private val navigator: Navigator

  init {
    navigator = Navigator(appContext)

    val api = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    moviesRepo = MoviesRepository(api)
  }

  fun getMoviesViewModel(fragment: Fragment): MoviesViewModel {
    return ViewModelProvider(fragment, MoviesViewModel.Factory(moviesRepo, navigator)).get(MoviesViewModel::class.java)
  }
}