package ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import ru.gaket.themoviedb.model.movies.entities.Movie
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.TerminalError
import ru.gaket.themoviedb.presentation.movies.viewmodel.ValidResult
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.Navigator
import java.util.concurrent.CancellationException

private const val DEBOUNCE_DELAY_TIME_MS = 500L

class MoviesViewModelImpl(val moviesRepository: MoviesRepository, val navigator: Navigator) : MoviesViewModel() {

    override val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    override val searchState = MutableLiveData<SearchState>()

    override val searchResult = queryChannel
        .asFlow()
        .debounce(DEBOUNCE_DELAY_TIME_MS)
        .onEach { searchState.value = Loading }
        .mapLatest {
            if (it.isEmpty()) {
                EmptyQuery
            } else {
                try {
                    val result = moviesRepository.searchMovies(it)
                    if (result.isEmpty()) {
                        EmptyResult
                    } else {
                        ValidResult(result)
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        Log.w(MoviesViewModelImpl::class.java.name, e)
                        ErrorResult(e)
                    }
                }
            }
        }
        .onEach { searchState.value = Ready }
        .catch { emit(TerminalError) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun onMovieAction(movie: Movie) {
        navigator.navigateTo("https://www.themoviedb.org/movie/${movie.id}")
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repo: MoviesRepository, private val navigator: Navigator) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MoviesViewModelImpl(moviesRepository = repo, navigator = navigator) as T
        }
    }
}
