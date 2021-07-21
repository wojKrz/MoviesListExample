package pl.movies.network.api

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class ClientModule {

    @[Provides Singleton]
    internal fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {

        val rxJavaCallAdapter = RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
        val gsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        return Retrofit.Builder()
            .addCallAdapterFactory(rxJavaCallAdapter)
            .addConverterFactory(gsonConverter)
            .client(client)
            .baseUrl("https://api.themoviedb.org/3/")
            .build()
    }

    @[Provides Singleton]
    internal fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

}
