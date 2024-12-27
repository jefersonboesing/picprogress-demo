package com.picprogress.viewmodel

import com.picprogress.viewmodel.AlbumConfigThemeViewModel.Action
import com.picprogress.viewmodel.AlbumConfigThemeViewModel.State
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.themes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumConfigThemeViewModel(
    currentTheme: AlbumTheme
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(selectedTheme = currentTheme))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun onSelectedAlbumThemeChange(albumTheme: AlbumTheme) = _uiState.update {
        it.copy(selectedTheme = albumTheme)
    }

    fun onBackClick() {
        runAction(Action.GoBack)
    }

    fun onApplyClicked() {
        runAction(Action.ApplyAlbumTheme(theme = _uiState.value.selectedTheme))
    }

    data class State(
        val theme: List<AlbumTheme> = themes,
        val selectedTheme: AlbumTheme = AlbumTheme.Lavender,
    )

    sealed class Action {
        data object GoBack : Action()
        data class ApplyAlbumTheme(val theme: AlbumTheme) : Action()
    }
}