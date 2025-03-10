package pl.movies.network.api.movies

import io.reactivex.rxjava3.core.Single
import pl.movies.network.api.paging.PageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = "e7213390e5734343d99dc18cba092dc8"
    ): PageDto<MovieDto>

    @GET("search/movie")
    suspend fun findMoviesByName(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = "e7213390e5734343d99dc18cba092dc8"
    ): PageDto<MovieDto>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") movieId: Long,
        @Query("api_key") apiKey: String = "e7213390e5734343d99dc18cba092dc8"
    ): Single<MovieDetailsDto>
}
