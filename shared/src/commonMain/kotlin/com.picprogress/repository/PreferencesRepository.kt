package com.picprogress.repository

import com.picprogress.model.preferences.Preferences

interface PreferencesRepository {
    suspend fun getPreferences(): Preferences
    suspend fun setCompareOverTutorialViewed(isViewed: Boolean)
}