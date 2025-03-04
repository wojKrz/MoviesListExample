package pl.movies.movieslist.ui.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChanges
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import pl.movies.movieslist.R
import pl.movies.movieslist.databinding.FragmentMoviesListBinding
import pl.movies.movieslist.ui.common.list.DataReloadType
import pl.movies.movieslist.ui.common.list.FooterReloadItem
import pl.movies.movieslist.ui.common.list.LoadingPageItem
import pl.movies.movieslist.ui.movieslist.MoviesListState.ErrorState
import pl.movies.movieslist.ui.movieslist.MoviesListState.SuccessState
import java.net.UnknownHostException

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

  private val viewModel: MoviesListViewModel by viewModels()

  private val disposables: CompositeDisposable =
    CompositeDisposable()

  private lateinit var searchDisposable: Disposable

  private val moviesListAdapter: GenericItemAdapter
    by lazy { GenericItemAdapter() }

  private val paginationLoadingAdapter: GenericItemAdapter
    by lazy { GenericItemAdapter() }

  private val mainAdapter: GenericFastItemAdapter by lazy {
    GenericFastItemAdapter()
      .also {
        it.addAdapter(0, moviesListAdapter)
        it.addAdapter(1, paginationLoadingAdapter)
      }
  }

  private lateinit var binding: FragmentMoviesListBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.refreshData()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMoviesListBinding.inflate(inflater, container, false)
    binding.lifecycleOwner = this
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initRecyclerView()
    initSwipeRefresh()

    observeListItems()

    searchDisposable = binding.searchInput
      .textChanges()
      .skipInitialValue()
      .map(CharSequence::toString)
      .subscribe {
        viewModel.searchMovies(it)
      }
    disposables.add(searchDisposable)
  }

  override fun onPause() {
    super.onPause()
    searchDisposable.dispose()
  }

  override fun onDestroy() {
    super.onDestroy()
    disposables.clear()
  }

  private val scrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      super.onScrollStateChanged(recyclerView, newState)

      // Do nothing when can scroll down.
      if (recyclerView.canScrollVertically(1))
        return

      // Here cannot scroll down and is settled - start download.
      if (newState == RecyclerView.SCROLL_STATE_IDLE)
        viewModel.loadNextPage()
    }
  }

  private fun initRecyclerView() {

    val moviesLayoutManager = GridLayoutManager(requireContext(), 2)
    moviesLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int =
        when (mainAdapter.getItem(position)?.type) {
          R.id.itemLoading, R.id.itemFooterReload -> 2
          else -> 1
        }
    }

    mainAdapter.addEventHook(FooterEventHook(::onReloadClick))
    mainAdapter.addEventHook(ToggleFavoriteMovieStatusEventHook(::onToggleFavoriteClick))
    mainAdapter.addEventHook(ClickMovieItemEventHook(::onMovieItemClick))

    binding.moviesRecycler.apply {
      adapter = mainAdapter
      layoutManager = moviesLayoutManager
      addOnScrollListener(scrollListener)
    }
  }

  private fun onReloadClick(reloadType: DataReloadType) {
    binding.moviesRecycler.addOnScrollListener(scrollListener)

    when (reloadType) {
      DataReloadType.NoMorePages, is DataReloadType.CustomType -> {
        viewModel.refreshData()
      }

      is DataReloadType.PagingError -> viewModel.loadNextPage()
    }
  }

  private fun onMovieItemClick(movieId: Long) {
    findNavController().navigate(
      MoviesListFragmentDirections.actionNavigateToMovieDetails(movieId)
    )
  }

  private fun onToggleFavoriteClick(movieId: Long) {
    viewModel.toggleMovieFavoriteStatus(movieId)
  }

  private fun initSwipeRefresh() {
    binding
      .swipeRefreshView
      .setOnRefreshListener {
        onReloadClick(DataReloadType.NoMorePages)
      }
  }

  private fun observeListItems() {
    viewModel
      .startObservingData()

    lifecycleScope.launch {
      viewModel
        .movies
        .collect {
          when (it) {
            is SuccessState -> {
              val items = it.movies.map(::NowPlayingMovieListItem)
              moviesListAdapter.setNewList(items)
              handleSuccessState(it)
            }

            is ErrorState -> handleErrorState(it)
          }
        }
    }
  }

  private fun handleSuccessState(state: SuccessState) {

    binding.swipeRefreshView.isRefreshing = state.isReloading

    if (state.noMoreItemsAvailable) {
      paginationLoadingAdapter.setNewList(listOf(FooterReloadItem(DataReloadType.NoMorePages)))
      binding.moviesRecycler.removeOnScrollListener(scrollListener)
    } else {
      paginationLoadingAdapter.setNewList(listOf(LoadingPageItem()))
    }
  }

  private fun handleErrorState(state: ErrorState) {

    val footerData = when (state.throwable) {
      is UnknownHostException -> DataReloadType.CustomType(
        R.string.network_error,
        R.string.paging_error_try_again
      )

      else -> DataReloadType.PagingError
    }

    binding.moviesRecycler.removeOnScrollListener(scrollListener)
    paginationLoadingAdapter.setNewList(listOf(FooterReloadItem(footerData)))
  }
}
