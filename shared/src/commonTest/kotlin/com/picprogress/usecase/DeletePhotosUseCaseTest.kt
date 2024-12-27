package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.photoMock
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class DeletePhotosUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = DeletePhotosUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val photos = listOf(photoMock())
        everySuspend { repository.deletePhotos(photos) } returns Unit

        // when
        val result = useCase.invoke(photos)

        // then
        verifySuspend { repository.deletePhotos(photos) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val photos = listOf(photoMock())
        everySuspend { repository.deletePhotos(photos) } throws Exception()

        // when
        val result = useCase.invoke(photos)

        // then
        verifySuspend { repository.deletePhotos(photos) }
        assertTrue(result is Result.Failure)
    }

}
