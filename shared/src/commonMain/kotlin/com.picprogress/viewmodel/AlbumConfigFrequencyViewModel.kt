package com.picprogress.viewmodel

import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel.Action
import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel.State
import com.picprogress.model.album.Frequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumConfigFrequencyViewModel(
    currentFrequency: Frequency
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(selectedFrequency = currentFrequency))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun onSelectedFrequencyChange(frequency: Frequency) = _uiState.update {
        it.copy(selectedFrequency = frequency)
    }

    fun onBackClick() {
        runAction(Action.GoBack)
    }

    fun onApplyClicked() {
        runAction(Action.ApplyAlbumFrequency(frequency = _uiState.value.selectedFrequency))
    }

    data class State(
        val frequencies: List<Frequency> = listOf(Frequency.DAILY, Frequency.WEEKLY, Frequency.MONTHLY),
        val selectedFrequency: Frequency = Frequency.DAILY,
    )

    sealed class Action {
        data object GoBack : Action()
        data class ApplyAlbumFrequency(val frequency: Frequency) : Action()
    }
}