package com.pedroid.data.remote.artists

import com.pedroid.data.remote.artists.dto.AlbumsDto
import com.pedroid.data.remote.artists.dto.ArtistsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface ArtistsApi {
    @GET("v1/me/top/artists")
    suspend fun getArtists(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): ArtistsDto

    @GET("v1/artists/{id}/albums")
    suspend fun getArtistAlbumsById(
        @Path("id") idArtist: String,
        @Query("offset") page: Int,
        @Query("limit") limit: Int
    ): AlbumsDto
}





