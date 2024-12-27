package com.picprogress.repository

import com.picprogress.local.data.dataset.AlbumDataSet
import com.picprogress.local.data.dataset.PhotoDataSet
import com.picprogress.local.storage.FileStorage
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.AlbumWithSummary
import com.picprogress.model.album.Frequency
import com.picprogress.model.album.isCurrent
import com.picprogress.model.album.toTimeRemaining
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.util.TimeFrameProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class AlbumsRepositoryImpl(
    private val albumDataSet: AlbumDataSet,
    private val timeFrameProvider: TimeFrameProvider,
    private val photoDataSet: PhotoDataSet,
    private val fileStorage: FileStorage
): AlbumsRepository {

    override suspend fun getAlbumsWithSummary(): List<AlbumWithSummary> {
        return albumDataSet.getAll().map { getAlbumWithSummary(it) }
    }

    override suspend fun getAlbumById(albumId: Long): Album? {
        return albumDataSet.getById(albumId)
    }

    override suspend fun saveAlbum(album: Album) {
        val isNewAlbum = albumDataSet.getById(album.id) == null
        if (isNewAlbum) {
            albumDataSet.insert(album)
        } else {
            albumDataSet.update(album)
        }
    }

    override suspend fun getPhotoById(photoId: Long): Photo? {
        return photoDataSet.getById(photoId)
    }

    override suspend fun getPhotosByAlbum(album: Album): List<Photo> {
        return photoDataSet.getAll(album)
    }

    override suspend fun getLastPhotoByAlbum(album: Album): Photo? {
        return photoDataSet.getLastOne(album)
    }

    override suspend fun addPhoto(album: Album, photoPath: PhotoPath) {
        val photoFile = fileStorage.save(photoPath, album.uuid)
        val photoTime = Instant.fromEpochMilliseconds(photoFile.modifiedAt).toLocalDateTime(TimeZone.currentSystemDefault())
        val photo = Photo(
            uuid = photoFile.uuid,
            photoPath = PhotoPath(photoFile.path),
            createdAt = photoTime
        )
        photoDataSet.insert(photo, album)
    }

    override suspend fun deletePhotos(photos: List<Photo>) {
        photoDataSet.delete(photos)
        photos.forEach { fileStorage.delete(it.photoPath) }
    }

    override suspend fun updatePhoto(photo: Photo) {
        fileStorage.updateFileDate(photo.photoPath, photo.createdAt)
        photoDataSet.update(photo)
    }

    override suspend fun deleteAlbum(album: Album) {
        deletePhotos(photos = getPhotosByAlbum(album))
        albumDataSet.delete(album)
    }

    private suspend fun getAlbumSummary(album: Album): AlbumSummary {
        val frames = when (album.frequency) {
            Frequency.DAILY -> timeFrameProvider.getCurrentWeekDaysTimeFrame()
            Frequency.WEEKLY -> timeFrameProvider.getWeeksTimeFrame(album.createdAt.date)
            Frequency.MONTHLY -> timeFrameProvider.getCurrentMonthsTimeFrame()
        }
        val currentFrame = frames.first { it.isCurrent() }
        val completedFrames = frames.filter { photoDataSet.getCountByTimeFrame(album, it) > 0 }

        val endDateTime = currentFrame.end.atTime(hour = 23, minute = 59, second = 59)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        return AlbumSummary(
            allFrames = frames,
            completedFrames = completedFrames,
            albumSize = photoDataSet.getCountByAlbum(album),
            timeRemaining = endDateTime.toInstant(TimeZone.currentSystemDefault())
                .minus(now.toInstant(TimeZone.currentSystemDefault())).toTimeRemaining(),
            firstPhotoDate = now,
            isUpdated = completedFrames.contains(currentFrame)
        )
    }

    private suspend fun getAlbumWithSummary(album: Album): AlbumWithSummary {
        return AlbumWithSummary(
            album = album,
            summary = getAlbumSummary(album)
        )
    }
}
