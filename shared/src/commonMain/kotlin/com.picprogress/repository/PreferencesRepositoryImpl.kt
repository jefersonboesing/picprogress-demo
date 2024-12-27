package com.picprogress.repository

import com.picprogress.local.data.dataset.PreferencesDataSet

class PreferencesRepositoryImpl(
    private val preferencesDataSet: PreferencesDataSet,
): PreferencesRepository {

    override suspend fun getPreferences() = preferencesDataSet.get()

    override suspend fun setCompareOverTutorialViewed(isViewed: Boolean) = preferencesDataSet.update(
        preferences = getPreferences().copy(
            isCompareOverTutorialViewed = isViewed
        )
    )

}