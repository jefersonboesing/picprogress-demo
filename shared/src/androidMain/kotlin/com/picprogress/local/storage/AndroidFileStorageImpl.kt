package com.picprogress.local.storage

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import com.picprogress.model.photo.PhotoPath
import com.picprogress.util.uuid
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.time.format.DateTimeFormatter

class AndroidFileStorageImpl(private val context: Context) : FileStorage {

    private val albumsFolder by lazy {
        File(context.filesDir, AlbumsFolder).also { it.mkdirs() }
    }

    override fun save(source: PhotoPath, albumUUID: String): PhotoFile {
        val photoUUID = uuid()
        val albumFolder = File(albumsFolder, albumUUID).also { it.mkdirs() }
        val outputFile = File(albumFolder, "$photoUUID.jpg")
        val sourceFile = File(source.path)
        sourceFile.inputStream().use { inputStream ->
            outputFile.outputStream().use {
                inputStream.copyTo(it)
            }
        }
        outputFile.setLastModified(Instant.fromEpochMilliseconds(sourceFile.lastModified()).toLocalDateTime(TimeZone.currentSystemDefault()))
        return PhotoFile(
            uuid = photoUUID,
            path = outputFile.path,
            modifiedAt = outputFile.lastModified().takeIf { it > 0 } ?: System.currentTimeMillis()
        )
    }

    override fun delete(source: PhotoPath): Boolean {
        return File(source.path).delete()
    }

    override fun updateFileDate(photoPath: PhotoPath, localDateTime: LocalDateTime) {
        File(photoPath.path).setLastModified(localDateTime)
    }

    private fun File.setLastModified(localDateTime: LocalDateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
        val datetime = localDateTime.toJavaLocalDateTime().format(formatter)
        ExifInterface(this).apply {
            setAttribute(ExifInterface.TAG_DATETIME, datetime)
            saveAttributes()
        }
        setLastModified(localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    }

    companion object {
        private const val AlbumsFolder = "albums"
    }

}


