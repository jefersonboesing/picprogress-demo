package com.picprogress.viewmodel

import com.picprogress.viewmodel.CompareViewModel.Action
import com.picprogress.viewmodel.CompareViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.ComparePhotos
import com.picprogress.model.photo.Photo
import com.picprogress.model.toast.ToastType
import com.picprogress.model.toast.ToastMessage
import com.picprogress.usecase.GetPreferencesUseCase
import com.picprogress.usecase.SetCompareOverTutorialViewedUseCase
import com.picprogress.viewmodel.args.CompareArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompareViewModel(
    private val args: CompareArgs,
    private val getPreferencesUseCase: GetPreferencesUseCase,
    private val setCompareOverTutorialViewedUseCase: SetCompareOverTutorialViewedUseCase,
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(album = args.album, comparePhotos = args.comparePhotos))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        dispatchGetPreferences()
    }

    fun onBackClicked() {
        runAction(Action.GoBack)
    }

    fun onChangePhotosClick() = with(uiState.value) {
        runAction(
            Action.GoToPhotoSelection(
                album = album,
                initialSelection = comparePhotos?.let { listOf(it.beforePhoto, it.afterPhoto) } ?: emptyList()
            )
        )
    }

    fun onComparePhotoSelectionChange(photos: List<Photo>) = _uiState.update {
        it.copy(
            comparePhotos = if (photos[0].createdAt < photos[1].createdAt) {
                ComparePhotos(beforePhoto = photos[0], afterPhoto = photos[1])
            } else {
                ComparePhotos(beforePhoto = photos[1], afterPhoto = photos[0])
            }
        )
    }

    fun onCompareModeChange(compareMode: CompareMode) = _uiState.update {
        it.copy(compareMode = compareMode)
    }

    fun onOverModeHold() {
        if (uiState.value.isCompareOverTutorialViewed) return
        _uiState.update { it.copy(isCompareOverTutorialViewed = true) }
        coroutineScope.launch { setCompareOverTutorialViewedUseCase.invoke(true) }
    }

    private fun dispatchGetPreferences() = coroutineScope.launch {
        val result = getPreferencesUseCase.invoke(Unit)
        if (result is Result.Success) {
            _uiState.update { it.copy(isCompareOverTutorialViewed = result.data.isCompareOverTutorialViewed) }
        }
    }

    sealed class Action {
        data object GoBack : Action()
        data class GoToPhotoSelection(val album: Album, val initialSelection: List<Photo>) : Action()
        data class ShowToast(val toastMessage: ToastMessage, val toastType: ToastType) : Action()
    }

    data class State(
        val album: Album,
        val comparePhotos: ComparePhotos?,
        val compareMode: CompareMode = CompareMode.SideBySide,
        val isCompareOverTutorialViewed: Boolean = false,
    )

    enum class CompareMode {
        SideBySide, Over
    }
}