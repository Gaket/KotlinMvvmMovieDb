package ru.gaket.themoviedb.di

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gaket.themoviedb.model.movies.network.MoviesApi
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository

class AppComponent {

    var moviesRepo: MoviesRepository

    init {
        val api = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesApi::class.java)

        moviesRepo = MoviesRepository(api)
    }
}