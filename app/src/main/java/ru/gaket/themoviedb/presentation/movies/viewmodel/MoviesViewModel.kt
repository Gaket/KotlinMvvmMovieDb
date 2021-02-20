package ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observer
import ru.gaket.themoviedb.model.movies.entities.Movie
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult

internal abstract class MoviesViewModel : ViewModel() {

    abstract val queryInput: Observer<String>

    abstract val searchResultOutput: LiveData<MoviesResult>
    abstract val searchStateOutput: LiveData<SearchState>

    abstract fun onMovieAction(movie: Movie)
}
