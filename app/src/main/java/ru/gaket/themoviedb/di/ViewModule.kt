package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.data.movies.MoviesRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModule {
	
	@Binds
	abstract fun bindMoviesRepository(
		impl: MoviesRepositoryImpl
	): MoviesRepository
}