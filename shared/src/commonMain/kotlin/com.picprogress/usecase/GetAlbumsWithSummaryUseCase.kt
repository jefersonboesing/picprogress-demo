package com.picprogress.usecase

import com.picprogress.model.album.AlbumWithSummary
import com.picprogress.repository.AlbumsRepository

class GetAlbumsWithSummaryUseCase(
    private val repository: AlbumsRepository
) : UseCase<Unit, List<AlbumWithSummary>>() {

    override suspend fun execute(param: Unit): List<AlbumWithSummary> {
        return repository.getAlbumsWithSummary()
    }

}