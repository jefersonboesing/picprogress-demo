package com.picprogress.usecase

import com.picprogress.model.preferences.Preferences
import com.picprogress.repository.PreferencesRepository

class GetPreferencesUseCase(
    private val repository: PreferencesRepository
) : UseCase<Unit, Preferences>() {

    override suspend fun execute(param: Unit): Preferences {
        return repository.getPreferences()
    }

}