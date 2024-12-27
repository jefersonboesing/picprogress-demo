package com.picprogress.usecase

import com.picprogress.repository.PreferencesRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue
import com.picprogress.model.Result
import dev.mokkery.answering.throws

class SetCompareOverTutorialViewedUseCaseTest {

    private val repository = mock<PreferencesRepository>()
    private val useCase = SetCompareOverTutorialViewedUseCase(repository)

    @Test
    fun `when calls invoke sets value successfully`() = runBlocking {
        // given
        val value = true
        everySuspend { repository.setCompareOverTutorialViewed(value) } returns Unit

        // when
        val result = useCase.invoke(value)

        // then
        verifySuspend { repository.setCompareOverTutorialViewed(value) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke throws exception`() = runBlocking {
        // given
        val value = false
        everySuspend { repository.setCompareOverTutorialViewed(value) } throws Exception()

        // when
        val result = useCase.invoke(value)

        // then
        verifySuspend { repository.setCompareOverTutorialViewed(value) }
        assertTrue(result is Result.Failure)
    }
}
