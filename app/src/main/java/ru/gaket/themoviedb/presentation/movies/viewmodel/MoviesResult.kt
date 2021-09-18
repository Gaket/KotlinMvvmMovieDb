package ru.gaket.themoviedb.presentation.movies.viewmodel

import ru.gaket.themoviedb.data.movies.db.MovieEntity

/**
 * Class containing the result of the [MovieEntity] request
 */
sealed class MoviesResult
class ValidResult(val result: List<MovieEntity>) : MoviesResult()
object EmptyResult : MoviesResult()
object EmptyQuery : MoviesResult()
class ErrorResult(val e: Throwable) : MoviesResult()
object TerminalError : MoviesResult()