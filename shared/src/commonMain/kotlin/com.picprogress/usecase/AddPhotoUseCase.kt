package com.picprogress.usecase

import com.picprogress.model.photo.AlbumPhoto
import com.picprogress.repository.AlbumsRepository

class AddPhotoUseCase(
    private val repository: AlbumsRepository
) : UseCase<AlbumPhoto, Unit>() {

    override suspend fun execute(param: AlbumPhoto) {
        return repository.addPhoto(param.album, param.photoPath)
    }

}