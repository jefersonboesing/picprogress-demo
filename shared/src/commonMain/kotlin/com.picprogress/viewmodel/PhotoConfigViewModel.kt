package com.picprogress.viewmodel

import com.picprogress.viewmodel.PhotoConfigViewModel.Action
import com.picprogress.viewmodel.PhotoConfigViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.photo.Photo
import com.picprogress.usecase.UpdatePhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime

class PhotoConfigViewModel(
    private val photo: Photo,
    private val updatePhotoUseCase: UpdatePhotoUseCase
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(originalDate = photo.createdAt.date, adjustedDate = photo.createdAt.date))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun onDateChanged(date: LocalDate) = _uiState.update {
        it.copy(adjustedDate = date)
    }

    fun onSaveClick() {
        coroutineScope.launch {
            val photoToUpdate = photo.copy(createdAt = _uiState.value.adjustedDate.atTime(photo.createdAt.time))
            val result = updatePhotoUseCase.invoke(photoToUpdate)
            if (result is Result.Success) runAction(Action.Close)
        }
    }

    sealed class Action {
        data object Close : Action()
    }

    data class State(
        val originalDate: LocalDate,
        val adjustedDate: LocalDate
    )
}