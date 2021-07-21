package pl.movies.movieslist.util

import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkException(): Boolean =
     this is UnknownHostException || this is SocketTimeoutException