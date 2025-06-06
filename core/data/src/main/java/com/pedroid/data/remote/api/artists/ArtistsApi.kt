package com.pedroid.data.remote.api.artists

import com.pedroid.data.remote.api.artists.dto.AlbumsDto
import com.pedroid.data.remote.api.artists.dto.ArtistsDto
import retrofit2.http.GET
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
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): AlbumsDto
}
