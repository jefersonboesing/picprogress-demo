package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.AlbumWithSummary
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

class GetAlbumsWithSummaryUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = GetAlbumsWithSummaryUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val albumsWithSummary = listOf(AlbumWithSummary(album = Album(), summary = AlbumSummary()))
        everySuspend { repository.getAlbumsWithSummary() } returns albumsWithSummary

        // when
        val result = useCase.invoke(Unit)

        // then
        verifySuspend { repository.getAlbumsWithSummary() }
        assertTrue(result is Result.Success)
        assertEquals(result.data, albumsWithSummary)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        everySuspend { repository.getAlbumsWithSummary() } throws Exception()

        // when
        val result = useCase.invoke(Unit)

        // then
        verifySuspend { repository.getAlbumsWithSummary() }
        assertTrue(result is Result.Failure)
    }
}
