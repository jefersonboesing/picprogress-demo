package com.picprogress.feature.photoselection

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.ds.util.ActionEffect
import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.bottomSheet
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.PhotoSelectionViewModel
import com.picprogress.viewmodel.PhotoSelectionViewModel.Action
import com.picprogress.viewmodel.args.PhotoSelectionArgs
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.photoSelectionSheet(
    onCloseClick: () -> Unit,
    onPhotosSelected: (List<Photo>) -> Unit,
) {
    bottomSheet(PhotoSelectionRoute) {
        val photoSelectionArgs = PhotoSelectionRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val viewModel: PhotoSelectionViewModel = koinViewModel(
            parameters = { parametersOf(photoSelectionArgs) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        PhotoSelectionSheet(
            state = uiState,
            onCloseClick = onCloseClick,
            onApplyClick = viewModel::onApplyClick,
            onPhotoClick = viewModel::onPhotoClick
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.Close -> onCloseClick()
                is Action.ReturnPhotoSelection -> onPhotosSelected(action.photos)
            }
        }
    }
}

fun NavController.navigateToPhotoSelection(
    album: Album,
    minRequired: Int,
    initialSelection: List<Photo> = emptyList(),
    unavailablePhotos: List<Photo> = emptyList(),
) {
    navigate(
        PhotoSelectionRoute.build(
            PhotoSelectionArgs(
                album = album,
                minRequired = minRequired,
                initialSelection = initialSelection,
                unavailablePhotos = unavailablePhotos
            )
        )
    )
}

object PhotoSelectionRoute : ArgAppRoute<PhotoSelectionArgs>("photo-selection")
