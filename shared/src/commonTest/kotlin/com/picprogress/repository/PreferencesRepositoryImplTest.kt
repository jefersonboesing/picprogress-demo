package com.picprogress.repository

import com.picprogress.local.data.dataset.PreferencesDataSet
import com.picprogress.model.preferences.Preferences
import dev.mokkery.answering.returns
import dev.mokkery.verifySuspend
import dev.mokkery.mock
import dev.mokkery.everySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class PreferencesRepositoryImplTest {

    private val preferencesDataSet = mock<PreferencesDataSet>()
    private val repository = PreferencesRepositoryImpl(preferencesDataSet)

    @Test
    fun `getPreferences returns preferences`() = runBlocking {
        // given
        val preferences = Preferences(isCompareOverTutorialViewed = false)
        everySuspend { preferencesDataSet.get() } returns preferences

        // when
        val result = repository.getPreferences()

        // then
        assertEquals(result, preferences)
    }

    @Test
    fun `setCompareOverTutorialViewed updates preferences`() = runBlocking {
        // given
        val newPreferences = Preferences(isCompareOverTutorialViewed = true)
        val currentPreferences = Preferences(isCompareOverTutorialViewed = false)
        everySuspend { preferencesDataSet.get() } returns currentPreferences
        everySuspend { preferencesDataSet.update(newPreferences) } returns Unit

        // when
        repository.setCompareOverTutorialViewed(true)

        // then
        verifySuspend { preferencesDataSet.update(newPreferences) }
    }
}
