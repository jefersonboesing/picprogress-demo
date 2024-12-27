package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class GetLastPhotoByAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Album, Photo?>() {

    override suspend fun execute(param: Album): Photo? {
        return repository.getLastPhotoByAlbum(param)
    }

}