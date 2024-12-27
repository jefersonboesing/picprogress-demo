package com.picprogress.viewmodel

import com.picprogress.viewmodel.PreviewViewModel.Action
import com.picprogress.viewmodel.PreviewViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.AlbumPhoto
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.model.toast.ToastMessage
import com.picprogress.model.toast.ToastType
import com.picprogress.usecase.AddPhotoUseCase
import com.picprogress.usecase.GetLastPhotoByAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviewViewModel(
    photoPath: PhotoPath,
    private val album: Album,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val getLastPhotoByAlbumUseCase: GetLastPhotoByAlbumUseCase
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(photoPath = photoPath))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun initialize() {
        dispatchGetLastPhoto()
    }

    fun onAddPhotoClick() {
        val photoPath = uiState.value.photoPath
        dispatchSavePhoto(photoPath)
    }

    fun onBackToCameraClick() {
        runAction(Action.GoBack)
    }

    fun onCloseClick() {
        runAction(Action.Close)
    }

    fun onChangeCompareOpacityClick() = _uiState.update {
        it.copy(isOpacityChecked = !it.isOpacityChecked)
    }

    private fun dispatchSavePhoto(photoPath: PhotoPath) = coroutineScope.launch {
        val result = addPhotoUseCase.invoke(AlbumPhoto(album = album, photoPath = photoPath))
        when (result) {
            is Result.Failure -> runAction(Action.ShowToast(ToastMessage.AddPhotoFailure, ToastType.Error))
            is Result.Success -> runAction(Action.Close)
        }
    }

    private fun dispatchGetLastPhoto() = coroutineScope.launch {
        val result = getLastPhotoByAlbumUseCase.invoke(album)
        if (result is Result.Success) {
            _uiState.update {
                it.copy(lastPhoto = result.data)
            }
        }
    }

    sealed class Action {
        data object Close : Action()
        data object GoBack : Action()
        data class ShowToast(val toastMessage: ToastMessage, val toastType: ToastType): Action()
    }

    data class State(
        val photoPath: PhotoPath,
        val isOpacityChecked: Boolean = false,
        val lastPhoto: Photo? = null,
    )
}