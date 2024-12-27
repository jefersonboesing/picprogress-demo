package com.picprogress.viewmodel

import com.picprogress.viewmodel.CameraViewModel.Action
import com.picprogress.viewmodel.CameraViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.camera.CameraLens
import com.picprogress.model.camera.FlashMode
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.usecase.GetLastPhotoByAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel(
    private val album: Album,
    private val getLastPhotoByAlbumUseCase: GetLastPhotoByAlbumUseCase
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State())
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun initialize() {
        dispatchGetLastPhoto()
    }

    fun onPhotoTaken(photoPath: PhotoPath) {
        runAction(Action.GoToPreview(album = album, photoPath = photoPath))
    }

    fun onChangeFlashModeClick() = _uiState.update { state ->
        state.copy(
            flashMode = when (state.flashMode) {
                FlashMode.OFF -> FlashMode.ON
                FlashMode.ON -> FlashMode.AUTO
                FlashMode.AUTO -> FlashMode.OFF
            }
        )
    }

    fun onChangeCameraLensClick() = _uiState.update { state ->
        state.copy(
            cameraLens = when (state.cameraLens) {
                CameraLens.FRONT -> CameraLens.BACK
                CameraLens.BACK -> CameraLens.FRONT
            }
        )
    }

    fun onCloseClick() {
        runAction(Action.Close)
    }

    fun onChangeCompareOpacityClick() = _uiState.update {
        it.copy(isOpacityChecked = !it.isOpacityChecked)
    }

    fun onCameraFailed() {
        if (uiState.value.cameraLens == CameraLens.BACK) {
            _uiState.update { it.copy(cameraLens = CameraLens.FRONT) }
        }
    }

    private fun dispatchGetLastPhoto() = coroutineScope.launch {
        val result = getLastPhotoByAlbumUseCase.invoke(album)
        if (result is Result.Success) {
            _uiState.update { it.copy(lastPhoto = result.data) }
        }
    }

    sealed class Action {
        data object Close : Action()
        data class GoToPreview(val album: Album, val photoPath: PhotoPath): Action()
    }

    data class State(
        val flashMode: FlashMode = FlashMode.OFF,
        val cameraLens: CameraLens = CameraLens.BACK,
        val isOpacityChecked: Boolean = true,
        val lastPhoto: Photo? = null
    )
}