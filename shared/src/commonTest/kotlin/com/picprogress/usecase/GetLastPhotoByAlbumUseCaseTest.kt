package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
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

class GetLastPhotoByAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = GetLastPhotoByAlbumUseCase(repository)

    @Test
    fun `when calls invoke returns success with photo`() = runBlocking {
        // given
        val album = Album()
        val photo = photoMock()
        everySuspend { repository.getLastPhotoByAlbum(album) } returns photo

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getLastPhotoByAlbum(album) }
        assertTrue(result is Result.Success)
        assertEquals(result.data, photo)
    }

    @Test
    fun `when calls invoke returns success with null`() = runBlocking {
        // given
        val album = Album()
        everySuspend { repository.getLastPhotoByAlbum(album) } returns null

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getLastPhotoByAlbum(album) }
        assertTrue(result is Result.Success)
        assertTrue(result.data == null)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val album = Album()
        everySuspend { repository.getLastPhotoByAlbum(album) } throws Exception()

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.getLastPhotoByAlbum(album) }
        assertTrue(result is Result.Failure)
    }
}
