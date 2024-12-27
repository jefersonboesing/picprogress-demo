package com.picprogress.local.data.mapper

import com.picprogress.model.preferences.Preferences
import compicprogress.PreferencesTable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PreferencesTableToPreferencesMapper : Mapper<PreferencesTable, Preferences> {
    override fun map(input: PreferencesTable): Preferences {
        return Json.decodeFromString(input.json)
    }
}