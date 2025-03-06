package pl.movies.movieslist.ui.common.list

import android.content.res.Resources
import dagger.Reusable
import pl.movies.movieslist.R
import javax.inject.Inject

@Reusable
class ErrorResources @Inject constructor(
    private val resources: Resources
) {

    fun getNetworkDataNotUpToDateText(): CharSequence = resources.getString(R.string.data_not_up_to_date)

    fun getGeneralErrorText(): CharSequence = resources.getString(R.string.common_error)
}
