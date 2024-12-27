package com.picprogress

import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.util.uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun photoMock(uuid: String = uuid(), photoPath: PhotoPath = PhotoPath(""), createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) = Photo(
    uuid = uuid,
    photoPath = photoPath,
    createdAt = createdAt
)

fun albumSummaryMock() = AlbumSummary()

