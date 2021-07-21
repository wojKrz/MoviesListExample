package pl.movies.movieslist.util

typealias ImagePath = String

fun ImagePath.withAppendedBaseUrl() = "https://image.tmdb.org/t/p/w500/$this"
