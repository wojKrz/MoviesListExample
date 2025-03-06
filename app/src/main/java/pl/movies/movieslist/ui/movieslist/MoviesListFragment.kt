package pl.movies.movieslist.ui.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import pl.movies.movieslist.databinding.FragmentMoviesListBinding
import pl.movies.movieslist.ui.movieslist.EventHandler.Event.ShowMovieDetails
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.RefreshData

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

  private val viewModel: MoviesListViewModel by viewModels()

  private val disposables: CompositeDisposable =
    CompositeDisposable()

  private lateinit var binding: FragmentMoviesListBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.handleIntent(RefreshData)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMoviesListBinding.inflate(inflater, container, false)
    binding.lifecycleOwner = this
    binding.composeView.apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        LaunchedEffect(EventHandler) {
          collectEvents()
        }
        Screen()
      }
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    observeListItems()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.clear()
  }

  private fun observeListItems() {
    viewModel.startObservingData()
  }

  private fun collectEvents() {
    viewLifecycleOwner.lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        EventHandler.events.collect { event ->
          when (event) {
            is ShowMovieDetails -> findNavController().navigate(
              MoviesListFragmentDirections.actionNavigateToMovieDetails(event.movieId)
            )
          }
        }
      }
    }
  }
}
