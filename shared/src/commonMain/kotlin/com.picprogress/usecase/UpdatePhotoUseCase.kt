package com.picprogress.usecase

import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class UpdatePhotoUseCase(
    private val repository: AlbumsRepository
) : UseCase<Photo, Unit>() {

    override suspend fun execute(param: Photo) {
        return repository.updatePhoto(photo = param)
    }

}