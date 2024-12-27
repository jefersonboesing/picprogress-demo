package com.picprogress.viewmodel

import com.picprogress.viewmodel.PhotoViewModel.Action
import com.picprogress.viewmodel.PhotoViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.ComparePhotos
import com.picprogress.model.photo.Photo
import com.picprogress.model.toast.ToastMessage
import com.picprogress.model.toast.ToastType
import com.picprogress.usecase.DeletePhotosUseCase
import com.picprogress.usecase.GetPhotoWithAlbumUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoViewModel(
    album: Album,
    photo: Photo,
    private val getPhotoWithAlbumUseCase: GetPhotoWithAlbumUseCase,
    private val deletePhotosUseCase: DeletePhotosUseCase
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State(album = album, photo = photo))
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun onCompareClick() = with(uiState.value) {
        runAction(Action.GoToPhotoSelection(album = album, unavailablePhotos = listOf(photo)))
    }

    fun onOptionsClick() = _uiState.update {
        it.copy(isMoreOptionsVisible = true)
    }

    fun onBackClick() {
        runAction(Action.GoBack)
    }

    fun onEditDateClick() {
        runAction(Action.GoToEdit(_uiState.value.photo))
        _uiState.update { it.copy(isMoreOptionsVisible = false) }
    }

    fun onDeleteClick() = _uiState.update {
        it.copy(
            isMoreOptionsVisible = false,
            isDeleteConfirmationVisible = true
        )
    }

    fun onDeleteConfirmClick() {
        coroutineScope.launch {
            _uiState.update { it.copy(isDeleteConfirmationVisible = false) }
            when (deletePhotosUseCase.invoke(param = listOf(_uiState.value.photo))) {
                is Result.Failure -> runAction(Action.ShowToast(ToastMessage.DeletePhotoFailure, ToastType.Error))
                is Result.Success -> runAction(Action.GoBack)
            }
        }
    }

    fun onConfirmationClose() = _uiState.update {
        it.copy(isDeleteConfirmationVisible = false)
    }

    fun onOptionsHidden() = _uiState.update {
        it.copy(isMoreOptionsVisible = false)
    }

    fun onRefresh() = coroutineScope.launch {
        dispatchGetPhoto()
    }

    fun onComparePhotoSelectionChange(photoToCompareWith: Photo) {
        val comparePhotos = if (photoToCompareWith.createdAt < uiState.value.photo.createdAt) {
            ComparePhotos(beforePhoto = photoToCompareWith, afterPhoto = uiState.value.photo)
        } else {
            ComparePhotos(beforePhoto = uiState.value.photo, afterPhoto = photoToCompareWith)
        }
        runAction(Action.GoToCompare(uiState.value.album, comparePhotos))
    }

    private fun dispatchGetPhoto() = coroutineScope.launch {
        val result = getPhotoWithAlbumUseCase.invoke(_uiState.value.photo.id)
        if (result is Result.Success) {
            _uiState.update {
                it.copy(photo = result.data.first, album = result.data.second)
            }
        }
    }

    sealed class Action {
        data object GoBack : Action()
        data class GoToCompare(val album: Album, val comparePhotos: ComparePhotos) : Action()
        data class GoToEdit(val photo: Photo) : Action()
        data class GoToPhotoSelection(val album: Album, val unavailablePhotos: List<Photo>) : Action()
        data class ShowToast(val toastMessage: ToastMessage, val toastType: ToastType) : Action()
    }

    data class State(
        val album: Album,
        val photo: Photo,
        val isMoreOptionsVisible: Boolean = false,
        val isDeleteConfirmationVisible: Boolean = false,
    )
}
