package pl.movies.domain.common

import java.lang.Exception

data class LceState<T>(
    val isLoading: Boolean,
    val content: T?,
    val error: Exception? = null
)
