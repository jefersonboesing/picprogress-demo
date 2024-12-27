package com.picprogress.local.storage

import com.picprogress.model.photo.PhotoPath
import kotlinx.datetime.LocalDateTime

interface FileStorage {
    fun save(source: PhotoPath, albumUUID: String): PhotoFile
    fun delete(source: PhotoPath): Boolean
    fun updateFileDate(photoPath: PhotoPath, localDateTime: LocalDateTime)
}