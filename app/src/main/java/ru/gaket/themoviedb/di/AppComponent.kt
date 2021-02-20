package ru.gaket.themoviedb.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gaket.themoviedb.BuildConfig
import ru.gaket.themoviedb.model.movies.network.MoviesApi
import ru.gaket.themoviedb.model.movies.repositories.MoviesRepository
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModelImpl
import ru.gaket.themoviedb.ru.gaket.themoviedb.core.SchedulerProvider
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.Navigator

/**
 * Class creating dependencies on the App level
 */
class AppComponent(appContext: Context) {

    private val moviesRepo: MoviesRepository
    private val navigator: Navigator
    private val schedulerProvider: SchedulerProvider

    init {
        navigator = Navigator(appContext)

        val api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)

        moviesRepo = MoviesRepository(api)
        schedulerProvider = SchedulerProvider.Impl()
    }

    internal fun getMoviesViewModel(fragment: Fragment): MoviesViewModelImpl {
        return ViewModelProvider(fragment, MoviesViewModelImpl.Factory(schedulerProvider, moviesRepo, navigator))
            .get(MoviesViewModelImpl::class.java)
    }
}
