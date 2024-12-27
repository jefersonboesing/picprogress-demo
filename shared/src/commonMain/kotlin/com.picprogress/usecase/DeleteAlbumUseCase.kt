package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.repository.AlbumsRepository

class DeleteAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Album, Unit>() {

    override suspend fun execute(param: Album) {
        return repository.deleteAlbum(param)
    }

}