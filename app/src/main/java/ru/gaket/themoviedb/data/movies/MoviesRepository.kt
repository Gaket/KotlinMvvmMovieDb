package ru.gaket.themoviedb.data.movies

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import ru.gaket.themoviedb.di.BaseImageUrlQualifier
import ru.gaket.themoviedb.data.movies.db.MovieEntity
import ru.gaket.themoviedb.data.movies.network.MoviesApi
import javax.inject.Inject

interface MoviesRepository {
	suspend fun searchMovies(query: String, page: Int = 1): List<MovieEntity>
}

/**
 * Repository providing data about [MovieEntity]
 */
class MoviesRepositoryImpl @Inject constructor(
	private val moviesApi: MoviesApi,
	@BaseImageUrlQualifier private val baseImageUrl: String
): MoviesRepository {
	
	/**
	 * Search [MovieEntity]s for the given [query] string
	 */
	@FlowPreview
	override suspend fun searchMovies(query: String, page: Int): List<MovieEntity> {
		return flowOf(moviesApi.searchMovie(query, page))
			.flowOn(Dispatchers.IO)
			.onEach { Log.d(MoviesRepositoryImpl::class.java.name, it.movies.toString()) }
			.flatMapMerge { searchResponse -> searchResponse.movies.asFlow() }
			.map { movieDto -> movieDto.toEntity(baseImageUrl) }
			.toList()
	}
}