package com.picprogress.viewmodel

import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.Frequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlbumConfigViewModel : BaseViewModel<Unit, Unit>() {
    var frequency: Frequency? = null
    var albumTheme: AlbumTheme? = null

    override val uiState: StateFlow<Unit> = MutableStateFlow(Unit)
}