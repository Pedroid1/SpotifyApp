package com.pedroid.domain.usecase.playlist

import com.pedroid.common.core.DataResource

interface CreatePlaylistUseCase {
    suspend fun execute(name: String): DataResource<Unit>
}