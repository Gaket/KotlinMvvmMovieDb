package ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gaket.themoviedb.model.movies.entities.Movie
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult
import ru.gaket.themoviedb.ru.gaket.themoviedb.core.SchedulerProvider
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.Navigator

private const val DEBOUNCE_DELAY_TIME_MS = 500L

internal class RxMoviesViewModelImpl(
    schedulerProvider: SchedulerProvider,
    private val moviesRepository: MoviesRepository,
    private val navigator: Navigator,
) : RxMoviesViewModel() {

    // TODO #2: use one of the subjects to get new query from the text changes flow.
    // Here it is enough to create publish subject
    override val queryInput = TODO()

    override val searchResultOutput = MutableLiveData<MoviesResult>()
    override val searchStateOutput = MutableLiveData<SearchState>()

    // TODO #3: create subscription class to collect all subscription you will do below.
    // Reminder: use CompositeDisposable class

    init {
        queryInput
        // TODO #4: in the init block we have to convert coroutines flow in MoviesViewModelImpl class to Rx.
        // Need to go operator by operator and convert each of it to Rx. Most of the popular operators
        // we covered on the lecture.
        // Skip asFlow() operator as we created Subject in TODO #2 earlier. Subject is already a Flow or Observable
        // Start with debounce. Add debounce operator and pass
        // DEBOUNCE_DELAY_TIME_MS as argument. Be ready that debounce operator operates on computation thread.
        // Check all debounce method definitions and choose the one with particular scheduler. You can use
        // schedulerProvider to get time scheduler and pass to debounce method

        // TODO #5: add operator which does the same as onEach. It is started with doOn... Set Loading
        // on searchState on each emission. Do not forget to use LiveData postMethod as our
        // chain will run on computation thread that was set in debounce method earlier

        // TODO #6: similar to mapLatest in Rx we have the same operator, which can cancel the previous
        // request for search movies and continue with new search. This operator will start inner chain which
        // we will build later in steps #6, #7, #8, #9 and #10

        // TODO #7: on step #6 operator will require to return result with type Observable<MoviesResult>.
        // In the body we have if/else clause. Let's start with condition when query is empty.
        // In this case you have to return EmptyQuery. But as we have to return type Observable<MoviesResult>
        // we need to use operator Observable.just to return the valid result

        // TODO #8: in else clause we have to search for movies. In MoviesRepository class we have a special method
        // called searchMovies(query: String). This one we need to use. However, this is a suspend function.
        // Do not worry we have special extensions like rxObservable from kotlinx.coroutines.rx2 import
        // For example, rxObservable { send(moviesRepository.searchMovies(query)) }

        // TODO #9: then we have to map the result we receive in step #8 to the Observable<MoviesResult>
        // Based on the result if the list of movies is empty we have to return EmptyResult or ValidResult otherwise

        // TODO #10: the return result should be MoviesResult not EmptyResult or ValidResult, so
        //  we have to cast it to MoviesResult by using operator cast

        // TODO #11: do not forget about exception we can receive while executing moviesRepository.searchMovies(query)
        // Our new chain rxObservable{...} can fail. In this case we have to return ErrorResult. Try to check operators
        // onError....

        // TODO #12: later we come back to our outer chain started in #3. All other work should be done on ui thread,
        // then we have to switch to main thread. Use operator observeOn and pass ui thread from schedulerProvider object
        // ask your mentor if you do not know why the next steps should run on ui thread

        // TODO #13: again for each emission we have to pass Ready to searchStateOutput. We did the same thing in step #4.
        // Use the same operator

        // TODO #14: start the emission by subscribing on the chain. Override 2 callbacks like onNext and onError.
        // onNext use to set value for searchResultOutput and onError use to set value TerminalError for searchResultOutput

        // TODO #15: add subscription to object we instantiated in step #2
    }

    override fun onMovieAction(movie: Movie) {
        navigator.navigateTo("https://www.themoviedb.org/movie/${movie.id}")
    }

    override fun onCleared() {
        super.onCleared()

        // TODO #16: do not forget to cancel all subscription after view model is destroyed
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val schedulerProvider: SchedulerProvider,
        private val repo: MoviesRepository,
        private val navigator: Navigator,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RxMoviesViewModelImpl(schedulerProvider, repo, navigator) as T
        }
    }
}
