package com.picprogress.local.data.dataset

import com.picprogress.model.preferences.Preferences

interface PreferencesDataSet {

    suspend fun update(preferences: Preferences)

    suspend fun get(): Preferences

    suspend fun delete()
}
