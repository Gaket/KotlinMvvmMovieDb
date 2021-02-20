package ru.gaket.themoviedb.presentation.movies.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.jakewharton.rxbinding3.widget.textChanges
import ru.gaket.themoviedb.MovieApp
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.databinding.MoviesFragmentBinding
import ru.gaket.themoviedb.presentation.movies.utils.hideKeyboard
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.TerminalError
import ru.gaket.themoviedb.presentation.movies.viewmodel.ValidResult
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.Loading
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModel
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.Ready
import ru.gaket.themoviedb.ru.gaket.themoviedb.presentation.movies.viewmodel.SearchState

class MoviesFragment : Fragment() {

    companion object {

        fun newInstance() = MoviesFragment()
    }

    private var _binding: MoviesFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    private lateinit var moviesAdapter: MoviesAdapter

    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MoviesFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity().application as MovieApp).myComponent.getMoviesViewModel(this)

        binding.moviesList.apply {
            val spanCount =
                // Set span count depending on layout
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> 4
                    else -> 2
                }
            layoutManager = GridLayoutManager(activity, spanCount)

            moviesAdapter = MoviesAdapter(viewModel::onMovieAction)
            adapter = moviesAdapter

            addItemDecoration(GridSpacingItemDecoration(spanCount,
                resources.getDimension(R.dimen.itemsDist).toInt(),
                true))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            })
        }

        binding.searchInput.textChanges()
            .map { text -> text.toString() }
            .distinctUntilChanged()
            .subscribe(viewModel.queryInput)

        viewModel.searchResultOutput.observe(viewLifecycleOwner, ::handleMoviesListResult)
        viewModel.searchStateOutput.observe(viewLifecycleOwner, ::handleLoadingState)
    }

    private fun handleLoadingState(state: SearchState) {
        when (state) {
            Loading -> {
                binding.searchIcon.visibility = View.GONE
                binding.searchProgress.visibility = View.VISIBLE
            }
            Ready -> {
                binding.searchIcon.visibility = View.VISIBLE
                binding.searchProgress.visibility = View.GONE
            }
        }
    }

    private fun handleMoviesListResult(result: MoviesResult) {
        when (result) {
            is ValidResult -> {
                binding.moviesPlaceholder.visibility = View.GONE
                binding.moviesList.visibility = View.VISIBLE
                moviesAdapter.submitList(result.result)
            }
            is ErrorResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.search_error)

                Log.e(MoviesFragment::class.java.name, "Something went wrong.", result.e)
            }
            is EmptyResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.empty_result)
            }
            is EmptyQuery -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.movies_placeholder)
            }
            is TerminalError -> {
                // Something wen't terribly wrong!
                println("Our Flow terminated unexpectedly, so we're bailing!")
                Toast.makeText(
                    activity,
                    getString(R.string.error_unknown_on_download),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
