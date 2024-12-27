package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.repository.AlbumsRepository

class SaveAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Album, Unit>() {

    override suspend fun execute(param: Album) {
        if (param.title.isEmpty()) throw InvalidTitleException()
        return repository.saveAlbum(param)
    }

    class InvalidTitleException : Exception()
}