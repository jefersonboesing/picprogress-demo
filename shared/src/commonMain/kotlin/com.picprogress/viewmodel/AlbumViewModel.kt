package com.picprogress.viewmodel

import com.picprogress.viewmodel.AlbumViewModel.Action
import com.picprogress.viewmodel.AlbumViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.AlbumPhoto
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.model.toast.ToastType
import com.picprogress.model.toast.ToastMessage
import com.picprogress.usecase.AddPhotoUseCase
import com.picprogress.usecase.DeleteAlbumUseCase
import com.picprogress.usecase.DeletePhotosUseCase
import com.picprogress.usecase.GetAlbumUseCase
import com.picprogress.usecase.GetPhotosByAlbumUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumViewModel(
    private var album: Album,
    private val getAlbumUseCase: GetAlbumUseCase,
    private val getPhotosByAlbumUseCase: GetPhotosByAlbumUseCase,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val deletePhotosUseCase: DeletePhotosUseCase,
    private val deleteAlbumUseCase: DeleteAlbumUseCase
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(State())
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun onRefresh() {
        dispatchGetAlbum()
    }

    fun onBackClick() {
        runAction(Action.Close)
    }

    fun onAddPhotoClick() {
        _uiState.update { it.copy(isAddPhotoOptionsVisible = true) }
    }

    fun onTakePictureClick() {
        runAction(Action.GoToCamera(album))
        _uiState.update { it.copy(isAddPhotoOptionsVisible = false) }
    }

    fun onUploadFromGalleryClick() {
        runAction(Action.ImportPhotoFromGallery)
        _uiState.update { it.copy(isAddPhotoOptionsVisible = false) }
    }

    fun onMoreOptionsClick() {
        _uiState.update { it.copy(isMoreOptionsVisible = true) }
    }

    fun onSelectPhotosClick() {
        _uiState.update {
            it.copy(
                isSelectionEnabled = true,
                selectedPhotos = emptyList(),
                isMoreOptionsVisible = false
            )
        }
    }

    fun onEditAlbumClick() {
        runAction(Action.GoToAlbumEdit(album))
        _uiState.update { it.copy(isMoreOptionsVisible = false) }
    }

    fun onDeleteAlbumClick() {
        _uiState.update { it.copy(isMoreOptionsVisible = false, isAlbumDeleteConfirmationVisible = true) }
    }

    fun onDeleteAlbumConfirmClick() {
        coroutineScope.launch {
            _uiState.update { it.copy(isAlbumDeleteConfirmationVisible = false) }
            when (deleteAlbumUseCase.invoke(album)) {
                is Result.Failure -> runAction(Action.ShowToast(ToastMessage.DeleteAlbumFailure, ToastType.Error))
                is Result.Success -> runAction(Action.Close)
            }
        }
    }

    fun onPhotoClick(photo: Photo) {
        if (_uiState.value.isSelectionEnabled) {
            _uiState.update {
                if (it.selectedPhotos.contains(photo)) {
                    it.copy(selectedPhotos = it.selectedPhotos - photo)
                } else {
                    it.copy(selectedPhotos = it.selectedPhotos + photo)
                }
            }
        } else {
            runAction(Action.GoToPhoto(album, photo))
        }
    }

    fun onOptionsHidden() {
        _uiState.update {
            it.copy(
                isAddPhotoOptionsVisible = false,
                isMoreOptionsVisible = false,
                isPhotoDeleteConfirmationVisible = false,
                isAlbumDeleteConfirmationVisible = false
            )
        }
    }

    fun onDeletePhotosClick() {
        _uiState.update { it.copy(isPhotoDeleteConfirmationVisible = true) }
    }

    fun onDeletePhotosConfirmClick()  {
        coroutineScope.launch {
            _uiState.update { it.copy(isPhotoDeleteConfirmationVisible = false) }
            val result = deletePhotosUseCase.invoke(param = _uiState.value.selectedPhotos)
            if (result is Result.Failure) {
                runAction(Action.ShowToast(ToastMessage.DeleteAlbumPhotosFailure, ToastType.Error))
            }
            _uiState.update { it.copy(isSelectionEnabled = false, selectedPhotos = emptyList()) }
            dispatchGetPhotos()
        }
    }

    fun onCancelSelectionClick() {
        _uiState.update { it.copy(isSelectionEnabled = false, selectedPhotos = emptyList()) }
    }

    fun onCompareClick() {
        runAction(Action.GoToCompare(album))
    }

    fun onPhotoImported(photoPath: PhotoPath) {
        dispatchSavePhoto(photoPath)
    }

    private fun dispatchGetAlbum() = coroutineScope.launch {
        if (uiState.value.isLoading) delay(500)
        when (val result = getAlbumUseCase.invoke(album.id)) {
            is Result.Failure -> runAction(Action.Close)
            is Result.Success -> {
                album = result.data
                _uiState.update { it.copy(title = album.title) }
                dispatchGetPhotos()
            }
        }
    }

    private fun dispatchGetPhotos() = coroutineScope.launch {
        val result = getPhotosByAlbumUseCase.invoke(album)
        if (result is Result.Success) {
            _uiState.update {
                it.copy(
                    photos = result.data,
                    isEmptyViewVisible = result.data.isEmpty(),
                    isLoading = false,
                    isCompareActionEnabled = result.data.size >= 2
                )
            }
        }
    }

    private fun dispatchSavePhoto(photoPath: PhotoPath) = coroutineScope.launch {
        addPhotoUseCase.invoke(AlbumPhoto(album = album, photoPath = photoPath))
        dispatchGetPhotos()
    }

    sealed class Action {
        data object Close : Action()
        data class GoToCamera(val album: Album) : Action()
        data class GoToAlbumEdit(val album: Album) : Action()
        data class GoToPhoto(val album: Album, val photo: Photo) : Action()
        data class GoToCompare(val album: Album) : Action()
        data class ShowToast(val toastMessage: ToastMessage, val toastType: ToastType) : Action()
        data object ImportPhotoFromGallery : Action()
    }

    data class State(
        val title: String = "",
        val photos: List<Photo> = emptyList(),
        val selectedPhotos: List<Photo> = emptyList(),
        val isSelectionEnabled: Boolean = false,
        val isAddPhotoOptionsVisible: Boolean = false,
        val isMoreOptionsVisible: Boolean = false,
        val isEmptyViewVisible: Boolean = false,
        val isPhotoDeleteConfirmationVisible: Boolean = false,
        val isAlbumDeleteConfirmationVisible: Boolean = false,
        val isCompareActionEnabled: Boolean = false,
        val isLoading: Boolean = true,
    )
}