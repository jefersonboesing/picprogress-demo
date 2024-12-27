package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.photoMock
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPhotosByAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = GetPhotosByAlbumUseCase(repository)

    @Test
    fun `when calls invoke returns success with photos`() = runBlocking {
        // given
        val album = Album()
        val photos = listOf(photoMock(), photoMock())
        everySuspend { repository.getPhotosByAlbum(album) } returns photos

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getPhotosByAlbum(album) }
        assertTrue(result is Result.Success)
        assertEquals(result.data, photos)
    }

    @Test
    fun `when calls invoke returns success with empty list`() = runBlocking {
        // given
        val album = Album()
        val photos = emptyList<Photo>()
        everySuspend { repository.getPhotosByAlbum(album) } returns photos

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getPhotosByAlbum(album) }
        assertTrue(result is Result.Success)
        assertEquals(result.data, photos)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val album = Album()
        everySuspend { repository.getPhotosByAlbum(album) } throws Exception()

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getPhotosByAlbum(album) }
        assertTrue(result is Result.Failure)
    }
}
