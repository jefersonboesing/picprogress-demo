@file:OptIn(ExperimentalMaterialNavigationApi::class)

package com.picprogress.feature.photo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.BottomSheetNavigatorSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.ds.util.ActionEffect
import com.picprogress.ds.components.toast.AppToastHost
import com.picprogress.ds.components.toast.rememberAppToastState
import com.picprogress.model.album.Album
import com.picprogress.model.photo.ComparePhotos
import com.picprogress.model.photo.Photo
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.ScreenResultKeys
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.PhotoViewModel
import com.picprogress.viewmodel.PhotoViewModel.Action
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.photoScreen(
    onBackClick: () -> Unit,
    onGoToCompare: (Album, ComparePhotos) -> Unit,
    onGoToEdit: (Photo) -> Unit,
    onOpenSelectToCompare: (Album, List<Photo>) -> Unit,
    bottomSheetNavigatorSheetState: BottomSheetNavigatorSheetState,
) {
    composable(PhotoRoute) {
        val photoArgs = PhotoRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val viewModel: PhotoViewModel = koinViewModel(
            parameters = { parametersOf(photoArgs.album, photoArgs.photo) }
        )

        val toastState = rememberAppToastState(coroutineScope = rememberCoroutineScope())
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        val photosToCompare: List<Photo> by it.savedStateHandle.getStateFlow<List<Photo>>(ScreenResultKeys.SelectedPhotosKey, emptyList())
            .collectAsStateWithLifecycle()

        PhotoScreen(
            state = uiState,
            onBackClick = viewModel::onBackClick,
            onCompareClick = viewModel::onCompareClick,
            onOptionsClick = viewModel::onOptionsClick,
            onEditDateClick = viewModel::onEditDateClick,
            onDeleteClick = viewModel::onDeleteClick,
            onOptionsHidden = viewModel::onOptionsHidden,
            onDeleteConfirmClick = viewModel::onDeleteConfirmClick,
            onConfirmationClose = viewModel::onConfirmationClose
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.GoBack -> onBackClick()
                is Action.GoToCompare -> onGoToCompare(action.album, action.comparePhotos)
                is Action.GoToEdit -> onGoToEdit(action.photo)
                is Action.GoToPhotoSelection -> onOpenSelectToCompare(action.album, action.unavailablePhotos)
                is Action.ShowToast -> toastState.show(action.toastMessage, action.toastType)
            }
        }

        LaunchedEffect(bottomSheetNavigatorSheetState.isVisible) {
            viewModel.onRefresh()
        }

        LaunchedEffect(photosToCompare) {
            if (photosToCompare.isNotEmpty()) {
                viewModel.onComparePhotoSelectionChange(photosToCompare.first())
                it.savedStateHandle.remove<List<Photo>>(ScreenResultKeys.SelectedPhotosKey)
            }
        }
        
        AppToastHost(state = toastState)
    }
}

fun NavController.navigateToPhoto(album: Album, photo: Photo) {
    navigate(PhotoRoute.build(PhotoArgs(album, photo)))
}

object PhotoRoute : ArgAppRoute<PhotoArgs>("photo")

@Serializable
data class PhotoArgs(val album: Album, val photo: Photo)