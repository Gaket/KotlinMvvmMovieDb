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
        val radius = 16.toPx
        val transformation: Transformation = RoundedCornersTransformation(radius, 0)
        Picasso.get()
                .load(movie.thumbnail)
                .placeholder(R.drawable.placeholder_movie_grey_200)
                .transform(transformation)
                .into(binding.movieThumbnail)
    }
}