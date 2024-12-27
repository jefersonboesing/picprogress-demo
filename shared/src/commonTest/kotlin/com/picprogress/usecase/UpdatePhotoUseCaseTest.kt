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

class UpdatePhotoUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = UpdatePhotoUseCase(repository)

    @Test
    fun `when calls invoke updates photo successfully`() = runBlocking {
        // given
        val photo = photoMock()
        everySuspend { repository.updatePhoto(photo) } returns Unit

        // when
        val result = useCase.invoke(photo)

        // then
        verifySuspend { repository.updatePhoto(photo) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke throws exception`() = runBlocking {
        // given
        val photo = photoMock()
        everySuspend { repository.updatePhoto(photo) } throws Exception()

        // when
        val result = useCase.invoke(photo)

        // then
        verifySuspend { repository.updatePhoto(photo) }
        assertTrue(result is Result.Failure)
    }
}
