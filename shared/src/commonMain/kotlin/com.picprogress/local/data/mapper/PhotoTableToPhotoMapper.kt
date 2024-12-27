package com.picprogress.local.data.mapper

import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import compicprogress.PhotoTable
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PhotoTableToPhotoMapper : Mapper<PhotoTable, Photo> {
    override fun map(input: PhotoTable): Photo {
        return Photo(
            id = input.id,
            uuid = input.uuid,
            photoPath = PhotoPath(input.path),
            createdAt = Instant.fromEpochMilliseconds(input.createdAt * 1000)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            albumId = input.albumId
        )
    }
}