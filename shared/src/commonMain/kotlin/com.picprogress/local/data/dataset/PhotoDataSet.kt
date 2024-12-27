package com.picprogress.local.data.dataset

import com.picprogress.local.data.mapper.PhotoTableToPhotoMapper
import com.picprogress.model.album.Album
import com.picprogress.model.album.TimeFrame
import com.picprogress.model.photo.Photo
import compicprogress.PhotoQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant

class PhotoDataSetImpl(
    private val photoQueries: PhotoQueries,
    private val photoTableToPhotoMapper: PhotoTableToPhotoMapper,
) : PhotoDataSet {

    override suspend fun insert(photo: Photo, album: Album) = withContext(Dispatchers.Default) {
        photoQueries.insert(
            uuid = photo.uuid,
            path = photo.photoPath.path,
            createdAt = photo.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            albumId = album.id
        )
    }

    override suspend fun update(photo: Photo) = withContext(Dispatchers.Default) {
        photoQueries.update(
            createdAt = photo.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            id = photo.id
        )
    }

    override suspend fun getById(photoId: Long): Photo? = withContext(Dispatchers.Default) {
        return@withContext photoQueries.getById(photoId).executeAsOneOrNull()?.let {
            photoTableToPhotoMapper.map(it)
        }
    }

    override suspend fun getLastOne(album: Album): Photo? = withContext(Dispatchers.Default) {
        return@withContext photoQueries.getLast(album.id).executeAsOneOrNull()?.let {
            photoTableToPhotoMapper.map(it)
        }
    }

    override suspend fun getAll(album: Album): List<Photo> = withContext(Dispatchers.Default) {
        return@withContext photoQueries.getAll(album.id).executeAsList().map {
            photoTableToPhotoMapper.map(it)
        }
    }

    override suspend fun delete(photos: List<Photo>) = withContext(Dispatchers.Default) {
        val photoIds = photos.map { it.id }
        return@withContext photoQueries.delete(photoIds)
    }

    override suspend fun deleteAll() = withContext(Dispatchers.Default) {
        photoQueries.deleteAll()
    }

    override suspend fun getCountByAlbum(album: Album): Int = withContext(Dispatchers.Default) {
        return@withContext photoQueries.getCountByAlbum(albumId = album.id).executeAsOne().toInt()
    }

    override suspend fun getCountByTimeFrame(album: Album, timeFrame: TimeFrame): Int = withContext(Dispatchers.Default) {
        return@withContext photoQueries.getByTimeFrame(
            albumId = album.id,
            startDate = timeFrame.start.atTime(hour = 0, minute = 0, second = 0)
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            endDate = timeFrame.end.atTime(hour = 23, minute = 59, second = 59)
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds
        ).executeAsList().size
    }

}