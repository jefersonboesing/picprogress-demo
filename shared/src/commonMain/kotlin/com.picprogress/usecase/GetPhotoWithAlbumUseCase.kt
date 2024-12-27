package com.picprogress.usecase

import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class GetPhotoWithAlbumUseCase(
    private val repository: AlbumsRepository
) : UseCase<Long, Pair<Photo, Album>>() {

    override suspend fun execute(param: Long): Pair<Photo, Album> {
        val photo = repository.getPhotoById(param) ?: throw IllegalStateException()
        val album = repository.getAlbumById(photo.albumId) ?: throw IllegalStateException()
        return photo to album
    }

}