package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.preferences.Preferences
import com.picprogress.repository.PreferencesRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPreferencesUseCaseTest {

    private val repository = mock<PreferencesRepository>()
    private val useCase = GetPreferencesUseCase(repository)

    @Test
    fun `when calls invoke returns preferences successfully`() = runBlocking {
        // given
        val preferences = Preferences(isCompareOverTutorialViewed = true)
        everySuspend { repository.getPreferences() } returns preferences

        // when
        val result = useCase.invoke(Unit)

        // then
        verifySuspend { repository.getPreferences() }
        assertTrue(result is Result.Success)
        assertEquals(result.data, preferences)
    }

    @Test
    fun `when calls invoke throws exception`() = runBlocking {
        // given
        everySuspend { repository.getPreferences() } throws Exception()

        // when
        val result = useCase.invoke(Unit)

        // then
        assertTrue(result is Result.Failure)
    }
}
