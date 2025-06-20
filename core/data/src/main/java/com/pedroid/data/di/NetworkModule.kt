package com.pedroid.data.di

import com.pedroid.data.Constants
import com.pedroid.data.remote.api.artists.ArtistsApi
import com.pedroid.data.remote.api.auth.AuthApi
import com.pedroid.data.remote.api.playlists.PlaylistsApi
import com.pedroid.data.remote.api.profile.ProfileApi
import com.pedroid.data.remote.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.SPOTIFY_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(defaultOkHttp())
        .build()

    @Provides
    @Named("SpotifyRetrofit")
    fun provideSpotifyRetrofit(
        @Named("AuthenticatedClient") client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.SPOTIFY_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    fun provideSpotifyAuthApi(@Named("AuthRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    fun provideSpotifyProfileApi(@Named("SpotifyRetrofit") retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides
    fun provideArtistsApi(@Named("SpotifyRetrofit") retrofit: Retrofit): ArtistsApi =
        retrofit.create(ArtistsApi::class.java)

    @Provides
    fun providePlaylistsApi(@Named("SpotifyRetrofit") retrofit: Retrofit): PlaylistsApi =
        retrofit.create(PlaylistsApi::class.java)

    @Provides
    @Named("AuthenticatedClient")
    fun authenticatedOkHttp(
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .addInterceptor(authInterceptor)
        .build()

    private fun defaultOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .build()

    private fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}
