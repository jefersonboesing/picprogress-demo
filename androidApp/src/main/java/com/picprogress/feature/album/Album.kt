@file:OptIn(ExperimentalMaterialNavigationApi::class)

package com.picprogress.feature.album

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.BottomSheetNavigatorSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.ds.util.ActionEffect
import com.picprogress.ds.screen.LoadingScreen
import com.picprogress.ds.components.toast.AppToastHost
import com.picprogress.ds.components.toast.rememberAppToastState
import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.util.getLastModified
import com.picprogress.util.toBitmapOrNull
import com.picprogress.util.toJpgTempFileOrNull
import com.picprogress.viewmodel.ActionState
import com.picprogress.viewmodel.AlbumViewModel
import com.picprogress.viewmodel.AlbumViewModel.Action
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterial3Api
fun NavGraphBuilder.albumScreen(
    onBackClick: () -> Unit,
    onOpenCameraClick: (Album) -> Unit,
    onOpenPhotoClick: (Album, Photo) -> Unit,
    onGoToCompare: (Album) -> Unit,
    onGoToEdit: (Album) -> Unit,
    bottomSheetNavigatorSheetState: BottomSheetNavigatorSheetState,
) {
    composable(AlbumRoute) {
        val args = AlbumRoute.getArgs(it.arguments)
        val viewModel: AlbumViewModel = koinViewModel(
            parameters = { parametersOf(args?.album ?: -1) }
        )
        val toastState = rememberAppToastState(coroutineScope = rememberCoroutineScope())
        val actionState: ActionState<Action> by viewModel.actionState.collectAsStateWithLifecycle()
        val uiState: AlbumViewModel.State by viewModel.uiState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val tempFile = uri?.toBitmapOrNull(context)?.toJpgTempFileOrNull(context, "gallery")
                ?: return@rememberLauncherForActivityResult
            tempFile.setLastModified(uri.getLastModified(context))
            viewModel.onPhotoImported(photoPath = PhotoPath(tempFile.path))
        }

        AnimatedContent(
            targetState = uiState.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(220, delayMillis = 90)).togetherWith(
                    fadeOut(animationSpec = tween(90))
                )
            },
            label = "album-loading"
        ) { isLoading ->
            when (isLoading) {
                true -> LoadingScreen()
                false -> AlbumScreen(
                    state = uiState,
                    onBackClick = viewModel::onBackClick,
                    onAddPhotoClick = viewModel::onAddPhotoClick,
                    onTakePictureClick = viewModel::onTakePictureClick,
                    onUploadFromGalleryClick = viewModel::onUploadFromGalleryClick,
                    onMoreOptionsClick = viewModel::onMoreOptionsClick,
                    onSelectPhotosClick = viewModel::onSelectPhotosClick,
                    onEditAlbumClick = viewModel::onEditAlbumClick,
                    onDeleteAlbumClick = viewModel::onDeleteAlbumClick,
                    onPhotoClick = viewModel::onPhotoClick,
                    onOptionsHidden = viewModel::onOptionsHidden,
                    onDeletePhotosClick = viewModel::onDeletePhotosClick,
                    onCancelSelectionClick = viewModel::onCancelSelectionClick,
                    onCompareClick = viewModel::onCompareClick,
                    onDeletePhotosConfirmClick = viewModel::onDeletePhotosConfirmClick,
                    onDeleteAlbumConfirmClick = viewModel::onDeleteAlbumConfirmClick,
                )
            }
        }

        ActionEffect(
            actionState = actionState,
            onActionStateProcessed = viewModel::onActionStateProcessed
        ) { action ->
            when (action) {
                Action.Close -> onBackClick()
                is Action.GoToAlbumEdit -> onGoToEdit(action.album)
                is Action.GoToCamera -> onOpenCameraClick(action.album)
                is Action.GoToPhoto -> onOpenPhotoClick(action.album, action.photo)
                is Action.GoToCompare -> onGoToCompare(action.album)
                Action.ImportPhotoFromGallery -> launcher.launch("image/*")
                is Action.ShowToast -> toastState.show(action.toastMessage, action.toastType)
            }
        }

        LaunchedEffect(bottomSheetNavigatorSheetState.isVisible) {
            viewModel.onRefresh()
        }

        AppToastHost(state = toastState)
    }
}

fun NavController.navigateToAlbum(album: Album) {
    navigate(
        AlbumRoute.build(
            args = AlbumNavArgs(album)
        )
    )
}

object AlbumRoute : ArgAppRoute<AlbumNavArgs>(name = "album")

@Serializable
data class AlbumNavArgs(val album: Album)
