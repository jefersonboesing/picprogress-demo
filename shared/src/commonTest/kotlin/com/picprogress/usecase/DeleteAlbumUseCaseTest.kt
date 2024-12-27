package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue


class DeleteAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = DeleteAlbumUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val album = Album()
        everySuspend { repository.deleteAlbum(album) } returns Unit

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.deleteAlbum(album) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val album = Album()
        everySuspend { repository.deleteAlbum(album) } throws Exception()

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.deleteAlbum(album) }
        assertTrue(result is Result.Failure)
    }

}
