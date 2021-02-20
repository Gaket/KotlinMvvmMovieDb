package ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.rx2.rxObservable
import ru.gaket.themoviedb.model.movies.entities.Movie
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository
import ru.gaket.themoviedb.ru.gaket.themoviedb.core.SchedulerProvider
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.Navigator
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.Loading
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModel
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.Ready
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.SearchState
import java.util.concurrent.TimeUnit.MILLISECONDS

private const val DEBOUNCE_DELAY_TIME_MS = 500L

@ExperimentalCoroutinesApi
@FlowPreview
internal class MoviesViewModelImpl(
    schedulerProvider: SchedulerProvider,
    private val moviesRepository: MoviesRepository,
    private val navigator: Navigator,
) : MoviesViewModel() {

    override val queryInput = PublishSubject.create<String>()

    override val searchResultOutput = MutableLiveData<MoviesResult>()
    override val searchStateOutput = MutableLiveData<SearchState>()

    private val subscriptions = CompositeDisposable()

    init {
        queryInput
            .debounce(DEBOUNCE_DELAY_TIME_MS, MILLISECONDS, schedulerProvider.time())
            .doOnEach { searchStateOutput.postValue(Loading) }
            .switchMap(::searchMoviesByQuery)
            .observeOn(schedulerProvider.ui())
            .doOnEach { searchStateOutput.value = Ready }
            .subscribeBy(onNext = searchResultOutput::setValue, onError = { searchResultOutput.value = TerminalError })
            .addTo(subscriptions)
    }

    private fun searchMoviesByQuery(query: String): Observable<MoviesResult> {
        return if (query.isEmpty()) {
            Observable.just(EmptyQuery)
        } else {
            rxObservable { send(moviesRepository.searchMovies(query)) }
                .map { result ->
                    if (result.isEmpty()) {
                        EmptyResult
                    } else {
                        ValidResult(result)
                    }
                }
                .cast<MoviesResult>()
                .onErrorReturn(::ErrorResult)
        }
    }

    override fun onMovieAction(movie: Movie) {
        navigator.navigateTo("https://www.themoviedb.org/movie/${movie.id}")
    }

    override fun onCleared() {
        super.onCleared()

        subscriptions.clear()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val schedulerProvider: SchedulerProvider,
        private val repo: MoviesRepository,
        private val navigator: Navigator,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MoviesViewModelImpl(schedulerProvider, repo, navigator) as T
        }
    }
}
