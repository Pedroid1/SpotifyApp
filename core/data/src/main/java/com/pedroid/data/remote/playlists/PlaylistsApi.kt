package com.pedroid.data.remote.playlists

import com.pedroid.data.remote.playlists.dto.PlaylistRequestDto
import com.pedroid.data.remote.playlists.dto.PlaylistsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistsApi {

    @GET("v1/me/playlists")
    suspend fun getPlaylists(
        @Query("offset") page: Int,
        @Query("limit") limit: Int
    ): PlaylistsDto

    @POST("v1/users/{userId}/playlists")
    suspend fun createPlaylist(
        @Path("userId") userId: String,
        @Body playlistRequest: PlaylistRequestDto
    )
}