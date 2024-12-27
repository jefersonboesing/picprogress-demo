package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.repository.AlbumsRepository

class GetAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Long, Album>() {

    override suspend fun execute(param: Long): Album {
        return repository.getAlbumById(param) ?: throw AlbumNotFoundException()
    }

    class AlbumNotFoundException : Exception()
}