package com.pedroid.data.remote.api.playlists

import com.pedroid.data.remote.api.playlists.dto.PlaylistRequestDto
import com.pedroid.data.remote.api.playlists.dto.PlaylistResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistsApi {

    @GET("v1/me/playlists")
    suspend fun getPlaylists(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PlaylistResponseDto

    @POST("v1/users/{userId}/playlists")
    suspend fun createPlaylist(
        @Path("userId") userId: String,
        @Body playlistRequest: PlaylistRequestDto
    )
}
