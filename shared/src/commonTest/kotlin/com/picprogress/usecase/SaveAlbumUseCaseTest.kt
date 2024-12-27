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
import kotlin.test.assertTrue

class SaveAlbumUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = SaveAlbumUseCase(repository)

    @Test
    fun `when calls invoke with valid album returns success`() = runBlocking {
        // given
        val album = Album(title = "Title")
        everySuspend { repository.saveAlbum(album) } returns Unit

        // when
        val result = useCase.invoke(album)

        // then
        verifySuspend { repository.saveAlbum(album) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke with empty title returns failure`() = runBlocking {
        // given
        val album = Album(title = "")

        // when
        val result = useCase.invoke(album)

        // then

        assertTrue(result is Result.Failure)
        assertTrue(result.exception is SaveAlbumUseCase.InvalidTitleException)
    }

    @Test
    fun `when calls invoke with invalid album throws exception`() = runBlocking {
        // given
        val album = Album(title = "")

        // when
        val result = useCase.invoke(album)

        // then
        assertTrue(result is Result.Failure)
        assertTrue(result.exception is SaveAlbumUseCase.InvalidTitleException)
    }
}
