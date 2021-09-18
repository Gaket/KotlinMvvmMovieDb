package ru.gaket.themoviedb.presentation.movies.view

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.ItemMovieBinding
import ru.gaket.themoviedb.data.movies.db.MovieEntity

class MovieViewHolder(
    private val binding: ItemMovieBinding
) : RecyclerView.ViewHolder(binding.root) {
	
	val transformation: Transformation
	
	init {
		val dimension = itemView.resources.getDimension(R.dimen.cornerRad)
		val cornerRadius = dimension.toInt()
		transformation = RoundedCornersTransformation(cornerRadius, 0)
	}
	
	fun bind(movieEntity: MovieEntity, onMovieClick: (MovieEntity) -> Unit) {
		setName(movieEntity)
		setThumbnail(movieEntity)
		setClickListener(onMovieClick, movieEntity)
	}
	
	private fun setClickListener(
		onMovieClick: (MovieEntity) -> Unit,
		movieEntity: MovieEntity
	) {
		itemView.setOnClickListener { onMovieClick(movieEntity) }
	}
	
	private fun setName(movieEntity: MovieEntity) {
		binding.movieName.text = movieEntity.name
	}
	
	private fun setThumbnail(movieEntity: MovieEntity) {
		Picasso.get()
			.load(movieEntity.thumbnail)
			.placeholder(R.drawable.ph_movie_grey_200)
			.error(R.drawable.ph_movie_grey_200)
			.transform(transformation)
			.fit()
			.centerCrop()
			.into(binding.movieThumbnail)
	}
}