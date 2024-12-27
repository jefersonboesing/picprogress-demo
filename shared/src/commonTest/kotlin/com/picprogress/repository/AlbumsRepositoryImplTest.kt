package com.picprogress.repository

import com.picprogress.local.data.dataset.AlbumDataSet
import com.picprogress.local.data.dataset.PhotoDataSet
import com.picprogress.local.storage.FileStorage
import com.picprogress.local.storage.PhotoFile
import com.picprogress.model.album.Album
import com.picprogress.model.album.Frequency
import com.picprogress.model.photo.PhotoPath
import com.picprogress.photoMock
import com.picprogress.util.TimeFrameProvider
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class AlbumsRepositoryImplTest {

    private val albumDataSet = mock<AlbumDataSet>()
    private val timeFrameProvider = mock<TimeFrameProvider>()
    private val photoDataSet = mock<PhotoDataSet>()
    private val fileStorage = mock<FileStorage>()
    private val repository = AlbumsRepositoryImpl(albumDataSet, timeFrameProvider, photoDataSet, fileStorage)

    @Test
    fun `getAlbumById returns album`() = runBlocking {
        // given
        val albumId = 1L
        val album = Album(id = albumId, frequency = Frequency.DAILY)
        everySuspend { albumDataSet.getById(albumId) } returns album

        // when
        val result = repository.getAlbumById(albumId)

        // then
        assertEquals(result, album)
    }

    @Test
    fun `saveAlbum inserts new album`() = runBlocking {
        // given
        val album = Album(id = 2L, frequency = Frequency.WEEKLY)
        everySuspend { albumDataSet.getById(album.id) } returns null
        everySuspend { albumDataSet.insert(album) } returns Unit

        // when
        repository.saveAlbum(album)

        // then
        verifySuspend { albumDataSet.insert(album) }
    }

    @Test
    fun `saveAlbum updates existing album`() = runBlocking {
        // given
        val album = Album(id = 1L, frequency = Frequency.MONTHLY)
        everySuspend { albumDataSet.getById(album.id) } returns album
        everySuspend { albumDataSet.update(album) } returns Unit

        // when
        repository.saveAlbum(album)

        // then
        verifySuspend { albumDataSet.update(album) }
    }

    @Test
    fun `getPhotoById returns photo`() = runBlocking {
        // given
        val photoId = 1L
        val photo = photoMock()
        everySuspend { photoDataSet.getById(photoId) } returns photo

        // when
        val result = repository.getPhotoById(photoId)

        // then
        assertEquals(result, photo)
    }

    @Test
    fun `addPhoto adds photo to album`() = runBlocking {
        // given
        val album = Album(id = 1L, frequency = Frequency.DAILY)
        val photoPath = PhotoPath("/path/to/photo")
        val modifiedAt = Clock.System.now().toEpochMilliseconds()
        val createdAt = Instant.fromEpochMilliseconds(modifiedAt).toLocalDateTime(TimeZone.currentSystemDefault())
        val file = PhotoFile("uuid", photoPath.path, modifiedAt)
        val photo = photoMock(uuid = file.uuid, photoPath = photoPath, createdAt = createdAt)
        everySuspend { fileStorage.save(photoPath, album.uuid) } returns file
        everySuspend { photoDataSet.insert(photo, album) } returns Unit

        // when
        repository.addPhoto(album, photoPath)

        // then
        verifySuspend { photoDataSet.insert(photo, album) }
    }

    @Test
    fun `deletePhotos deletes photos and files`() = runBlocking {
        // given
        val photo = photoMock()
        everySuspend { photoDataSet.delete(listOf(photo)) } returns Unit
        everySuspend { fileStorage.delete(photo.photoPath) } returns true

        // when
        repository.deletePhotos(listOf(photo))

        // then
        verifySuspend { photoDataSet.delete(listOf(photo)) }
        verifySuspend { fileStorage.delete(photo.photoPath) }
    }

    @Test
    fun `getLastPhotoByAlbum returns last photo`() = runBlocking {
        // given
        val album = Album(id = 1L, frequency = Frequency.MONTHLY)
        val photo = photoMock()
        everySuspend { photoDataSet.getLastOne(album) } returns photo

        // when
        val result = repository.getLastPhotoByAlbum(album)

        // then
        assertEquals(result, photo)
    }

}
