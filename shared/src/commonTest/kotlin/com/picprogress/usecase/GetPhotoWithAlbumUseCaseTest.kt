package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.photoMock
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPhotoWithAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = GetPhotoWithAlbumUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val photoId = 1L
        val photo = photoMock()
        val album = Album()
        everySuspend { repository.getPhotoById(photoId) } returns photo
        everySuspend { repository.getAlbumById(photo.albumId) } returns album

        // when
        val result = useCase.invoke(photoId)

        // then
        verifySuspend { repository.getPhotoById(photoId) }
        verifySuspend { repository.getAlbumById(photo.albumId) }
        assertTrue(result is Result.Success)
        assertEquals(result.data.first, photo)
        assertEquals(result.data.second, album)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val photoId = 1L
        everySuspend { repository.getPhotoById(photoId) } returns null

        // when
        val result = useCase.invoke(photoId)

        // then
        verifySuspend { repository.getPhotoById(photoId) }
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `when calls invoke returns failure when album is not found`() = runBlocking {
        // given
        val photoId = 1L
        val photo = photoMock()
        everySuspend { repository.getPhotoById(photoId) } returns photo
        everySuspend { repository.getAlbumById(photo.albumId) } returns null

        // when
        val result = useCase.invoke(photoId)

        // then
        verifySuspend { repository.getPhotoById(photoId) }
        verifySuspend { repository.getAlbumById(photo.albumId) }
        assertTrue(result is Result.Failure)
    }
}
