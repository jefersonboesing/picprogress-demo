package com.picprogress.usecase

import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class DeletePhotosUseCase(
    private val repository: AlbumsRepository
) : UseCase<List<Photo>, Unit>() {

    override suspend fun execute(param: List<Photo>) {
        return repository.deletePhotos(param)
    }

}