@file:OptIn(ExperimentalForeignApi::class)

package com.picprogress.local.storage

import com.picprogress.model.photo.PhotoPath
import com.picprogress.util.append
import com.picprogress.util.create
import com.picprogress.util.getAbsolutePath
import com.picprogress.util.getDocumentsDirectory
import com.picprogress.util.throwError
import kotlinx.datetime.LocalDateTime

import platform.Foundation.*
import kotlinx.cinterop.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalForeignApi::class)
class iOSFileStorage : FileStorage {

    private val fileManager = NSFileManager.defaultManager

    private val albumsFolder: NSURL by lazy {
        fileManager.getDocumentsDirectory().append("albums").create()
    }

    override fun save(source: PhotoPath, albumUUID: String): PhotoFile {
        val photoUUID = NSUUID().UUIDString
        val albumFolder = albumsFolder.append(albumUUID).create()
        val outputURL = albumFolder.append("$photoUUID.png")
        val sourcePath = source.getAbsolutePath()

        fileManager.contentsAtPath(sourcePath)?.writeToURL(outputURL, true) ?: throw RuntimeException()

        val fileAttributes = fileManager.attributesOfItemAtPath(source.path, null)
        val creationDate = fileAttributes?.get(NSFileCreationDate) as? NSDate ?: NSDate()

        return PhotoFile(
            uuid = photoUUID,
            path = outputURL.path!!.removePrefix(fileManager.getDocumentsDirectory().path!!),
            modifiedAt = creationDate.toEpochMilliseconds()
        ).also { photoFile ->
            updateFileDate(photoFile.toPhotoPath(), photoFile.getModifiedAtDateTime())
            delete(source)
        }
    }

    override fun delete(source: PhotoPath): Boolean {
        return fileManager.removeItemAtPath(source.getAbsolutePath(), null)
    }

    override fun updateFileDate(photoPath: PhotoPath, localDateTime: LocalDateTime) {
        val date = NSDate.dateWithTimeIntervalSince1970(
            localDateTime.toInstant(TimeZone.currentSystemDefault())
                .toEpochSeconds()
                .toDouble()
        )
        throwError { errorPointer ->
            fileManager.setAttributes(
                attributes = mapOf(NSFileCreationDate to date),
                ofItemAtPath = photoPath.getAbsolutePath(),
                error = errorPointer
            )
        }
    }

    private fun Instant.toEpochSeconds(): Long {
        return toEpochMilliseconds() / 1000
    }

    private fun PhotoFile.getModifiedAtDateTime(): LocalDateTime {
        return Instant.fromEpochMilliseconds(modifiedAt).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private fun PhotoFile.toPhotoPath() = PhotoPath(path)

    private fun NSDate.toEpochMilliseconds(): Long {
        return timeIntervalSince1970.times(1000).toLong()
    }
}


