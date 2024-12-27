package com.picprogress.viewmodel

import com.picprogress.viewmodel.AlbumConfigMainViewModel.Action
import com.picprogress.viewmodel.AlbumConfigMainViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.Frequency
import com.picprogress.model.album.themes
import com.picprogress.usecase.SaveAlbumUseCase
import com.picprogress.util.TimeFrameProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumConfigMainViewModel(
    album: Album?,
    private val saveAlbumUseCase: SaveAlbumUseCase,
    private val timeFrameProvider: TimeFrameProvider,
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(
        State(
            album = album ?: Album(theme = themes.random()),
            isNew = album == null
        )
    )
    override val uiState = _uiState.asStateFlow()

    init {
        refreshAlbumSummary()
    }

    fun onTitleChange(title: String) {
        if (title.length > 30 && title.length > uiState.value.album.title.length) return
        _uiState.update {
            it.copy(album = it.album.copy(title = title), isTitleInvalid = false)
        }
    }

    fun onNotesChange(notes: String) = _uiState.update {
        it.copy(album = it.album.copy(notes = notes))
    }

    fun onAlbumThemeChange(albumTheme: AlbumTheme) = _uiState.update {
        it.copy(album = it.album.copy(theme = albumTheme))
    }

    fun onAlbumFrequencyChange(frequency: Frequency) = _uiState.update {
        it.copy(album = it.album.copy(frequency = frequency))
    }.also { refreshAlbumSummary() }

    fun onSaveAlbumClick() {
        dispatchSaveAlbum()
    }

    fun onGoToChangeFrequencyClick() {
        runAction(Action.GoToChangeFrequency(current = uiState.value.album.frequency))
    }

    fun onGoToChangeThemeClick() {
        runAction(Action.GoToChangeTheme(current = uiState.value.album.theme))
    }

    fun onCloseClick() {
        runAction(Action.Close)
    }

    private fun refreshAlbumSummary() = _uiState.update {
        val timeFrames = when (it.album.frequency) {
            Frequency.DAILY -> timeFrameProvider.getCurrentWeekDaysTimeFrame()
            Frequency.WEEKLY -> timeFrameProvider.getWeeksTimeFrame()
            Frequency.MONTHLY -> timeFrameProvider.getCurrentMonthsTimeFrame()
        }
        it.copy(summary = AlbumSummary(allFrames = timeFrames, completedFrames = emptyList()))
    }

    private fun dispatchSaveAlbum() = coroutineScope.launch {
        when (val result = saveAlbumUseCase.invoke(_uiState.value.album)) {
            is Result.Failure -> {
                if (result.exception is SaveAlbumUseCase.InvalidTitleException) {
                    _uiState.update { it.copy(isTitleInvalid = true) }
                }
            }
            is Result.Success -> runAction(Action.Close)
        }
    }

    sealed class Action {
        data object Close : Action()
        data class GoToChangeTheme(val current: AlbumTheme) : Action()
        data class GoToChangeFrequency(val current: Frequency) : Action()
    }

    data class State(
        val album: Album,
        val isTitleInvalid: Boolean = false,
        val isLoading: Boolean = false,
        val summary: AlbumSummary = AlbumSummary(),
        val isNew: Boolean = true
    )
}