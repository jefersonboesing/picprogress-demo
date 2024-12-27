package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class GetPhotosByAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Album, List<Photo>>() {

    override suspend fun execute(param: Album): List<Photo> {
        return repository.getPhotosByAlbum(param)
    }

}