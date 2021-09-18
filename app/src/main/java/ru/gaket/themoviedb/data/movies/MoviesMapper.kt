package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.movies.db.MovieEntity
import ru.gaket.themoviedb.data.movies.network.MovieDto

fun MovieDto.toEntity(baseImageUrl: String) = MovieEntity(
	id = id,
	name = title,
	thumbnail = getPosterUrl(baseImageUrl, posterPath)
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?) =
	"${baseImageUrl}${posterPath}"