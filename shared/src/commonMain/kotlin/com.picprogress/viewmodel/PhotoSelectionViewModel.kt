package com.picprogress.viewmodel

import com.picprogress.viewmodel.PhotoSelectionViewModel.Action
import com.picprogress.viewmodel.PhotoSelectionViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.photo.Photo
import com.picprogress.usecase.GetPhotosByAlbumUseCase
import com.picprogress.viewmodel.args.PhotoSelectionArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoSelectionViewModel(
    private val args: PhotoSelectionArgs,
    private val getPhotosByAlbumUseCase: GetPhotosByAlbumUseCase,
) : BaseViewModel<State, Action>() {

    private val _uiState = MutableStateFlow(
        State(
            minRequired = args.minRequired,
            selectedPhotos = args.initialSelection
        )
    )
    override val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        dispatchGetPhotos()
    }

    fun onPhotoClick(photo: Photo) = _uiState.update {
        it.copy(
            selectedPhotos = if (it.selectedPhotos.contains(photo)) {
                it.selectedPhotos - photo
            } else if (it.selectedPhotos.size < it.minRequired) {
                it.selectedPhotos + photo
            } else {
                it.selectedPhotos
            }
        )
    }

    fun onApplyClick() {
        runAction(Action.ReturnPhotoSelection(photos = uiState.value.selectedPhotos))
    }

    private fun dispatchGetPhotos() = coroutineScope.launch {
        val result = getPhotosByAlbumUseCase.invoke(args.album)
        if (result is Result.Success) {
            _uiState.update {
                it.copy(photos = result.data - args.unavailablePhotos.toSet())
            }
        }
    }

    sealed class Action {
        data object Close : Action()
        data class ReturnPhotoSelection(val photos: List<Photo>) : Action()
    }

    data class State(
        val photos: List<Photo> = emptyList(),
        val selectedPhotos: List<Photo> = emptyList(),
        val minRequired: Int = 1,
    ) {
        val isReady: Boolean
            get() = selectedPhotos.size >= minRequired
    }
}