package pl.movies.network.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.movies.network.api.movies.MoviesApi
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class ApiModule {

    @[Provides Singleton]
    internal fun provideMoviesApi(retrofit: Retrofit): MoviesApi =
        retrofit.create()

}
