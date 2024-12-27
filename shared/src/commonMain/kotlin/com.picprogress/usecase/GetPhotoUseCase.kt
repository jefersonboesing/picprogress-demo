package com.picprogress.usecase

import com.picprogress.model.photo.Photo
import com.picprogress.repository.AlbumsRepository

class GetPhotoUseCase(
    private val repository: AlbumsRepository
) : UseCase<Long, Photo>() {

    override suspend fun execute(param: Long): Photo {
        return repository.getPhotoById(param) ?: throw IllegalStateException()
    }

}