package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = GetAlbumUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val albumId = 1L
        val album = Album()
        everySuspend { repository.getAlbumById(albumId) } returns album

        // when
        val result = useCase.invoke(albumId)

        // then
        verifySuspend { repository.getAlbumById(albumId) }
        assertTrue(result is Result.Success)
        assertEquals(result.data, album)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val albumId = 1L
        everySuspend { repository.getAlbumById(albumId) } returns null

        // when
        val result = useCase.invoke(albumId)

        // then
        verifySuspend { repository.getAlbumById(albumId) }
        assertTrue(result is Result.Failure)
    }
}
