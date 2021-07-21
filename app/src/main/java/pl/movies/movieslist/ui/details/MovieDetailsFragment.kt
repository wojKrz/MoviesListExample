package pl.movies.movieslist.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.movies.movieslist.databinding.FragmentMovieDetailsBinding

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val disposables: CompositeDisposable =
        CompositeDisposable()

    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.disposables = disposables
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        observeDetails()
        getMovieDetails()
        observeError()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun initToolbar() {
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
    }

    private fun observeDetails() {
        val movieId = navArgs<MovieDetailsFragmentArgs>().value.movieId
        viewModel.startObservingDetails(movieId)
    }

    private fun getMovieDetails() {
        viewModel.fetchMovieDetails()
    }

    private fun observeError() {
        viewModel
            .error
            .observe(viewLifecycleOwner) {
                handleErrorText(it)
            }
    }

    private fun handleErrorText(text: CharSequence) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }
}
