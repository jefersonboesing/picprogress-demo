package com.picprogress.local.data.dataset

import com.picprogress.local.data.mapper.PreferencesTableToPreferencesMapper
import com.picprogress.model.preferences.Preferences
import compicprogress.PreferencesQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesDataSetImpl (
    private val preferencesQueries: PreferencesQueries,
    private val preferencesTableToPreferencesMapper: PreferencesTableToPreferencesMapper,
): PreferencesDataSet {

    override suspend fun update(preferences: Preferences) = withContext(Dispatchers.Default) {
        val current = preferencesQueries.get().executeAsOneOrNull()
        val jsonPreferences = Json.encodeToString(preferences)
        if (current == null) {
            preferencesQueries.insert(jsonPreferences)
        } else {
            preferencesQueries.update(jsonPreferences, id = current.id)
        }
    }

    override suspend fun get(): Preferences = withContext(Dispatchers.Default) {
        return@withContext preferencesQueries.get().executeAsOneOrNull()?.let { preferencesTableToPreferencesMapper.map(it) } ?: Preferences()
    }

    override suspend fun delete() = withContext(Dispatchers.Default) {
        return@withContext preferencesQueries.delete()
    }

}