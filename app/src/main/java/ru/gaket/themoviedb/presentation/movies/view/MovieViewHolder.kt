package ru.gaket.themoviedb.presentation.movies.view

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.ItemMovieBinding
import ru.gaket.themoviedb.model.movies.entities.Movie
import ru.gaket.themoviedb.presentation.movies.utils.toPx


class MovieViewHolder(private val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

  val transformation: Transformation

  init {
    val dimension = itemView.resources.getDimension(R.dimen.cornerRad)
    val cornerRadius = dimension.toInt()
    transformation = RoundedCornersTransformation(cornerRadius, 0)
  }

  fun bind(movie: Movie, listener: (Movie) -> Unit) {
    setName(movie)
    setThumbnail(movie)
    setClickListener(listener, movie)
  }

  private fun setClickListener(
      listener: (Movie) -> Unit,
      movie: Movie
  ) {
    itemView.setOnClickListener { listener(movie) }
  }

  private fun setName(movie: Movie) {
    binding.movieName.text = movie.name
  }

  private fun setThumbnail(movie: Movie) {
    Picasso.get()
        .load(movie.thumbnail)
        .placeholder(R.drawable.ph_movie_grey_200)
        .error(R.drawable.ph_movie_grey_200)
        .fit()
        .centerCrop()
        .into(binding.movieThumbnail)
  }
}