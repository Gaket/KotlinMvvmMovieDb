package ru.gaket.themoviedb.data.movies.db

/**
 * Business class of Movies
 */
data class MovieEntity(
	val id: Int,
	val name: String,
	val thumbnail: String?
)