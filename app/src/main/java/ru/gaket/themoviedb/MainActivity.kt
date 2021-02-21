package ru.gaket.themoviedb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.view.MoviesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                // TODO #1: MoviesFragment is a working coroutine example which uses MoviesViewModelImpl.
                // In order to check your RxMoviesViewModelImpl you have to change fragment from MoviesFragment
                // to RxMoviesFragment. It will use RxMoviesViewModelImpl
                .replace(R.id.container, MoviesFragment.newInstance())
                .commitNow()
        }
    }
}
