package com.picprogress.usecase

import com.picprogress.repository.PreferencesRepository

class SetCompareOverTutorialViewedUseCase(
    private val repository: PreferencesRepository
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(param: Boolean) {
        return repository.setCompareOverTutorialViewed(param)
    }

}