package com.picprogress.feature.camera

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.picprogress.ds.util.ActionEffect
import com.picprogress.ds.util.StatusBarEffect
import com.picprogress.model.album.Album
import com.picprogress.model.photo.PhotoPath
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.CameraViewModel
import com.picprogress.viewmodel.CameraViewModel.Action
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.cameraScreen(
    onCloseClick: () -> Unit,
    onGoToPreview: (album: Album, photoPath: PhotoPath) -> Unit,
) {
    composable(CameraRoute) {
        val cameraArgs = CameraRoute.getArgs(it.arguments)
        val viewModel: CameraViewModel = koinViewModel(
            parameters = { parametersOf(cameraArgs?.album) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        CameraScreen(
            state = uiState,
            onPhotoTaken = { file -> viewModel.onPhotoTaken(PhotoPath(file.path)) },
            onChangeFlashModeClick = viewModel::onChangeFlashModeClick,
            onChangeCameraLensClick = viewModel::onChangeCameraLensClick,
            onCloseClick = viewModel::onCloseClick,
            onChangeCompareOpacityClick = viewModel::onChangeCompareOpacityClick,
            onCameraFailed = viewModel::onCameraFailed
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.Close -> onCloseClick()
                is Action.GoToPreview -> onGoToPreview(action.album, action.photoPath)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.initialize()
        }

        StatusBarEffect(isAppearanceLightStatusBarsForScreen = false)
    }
}

fun NavController.navigateToCamera(album: Album) {
    navigate(CameraRoute.build(CameraArgs(album)))
}

object CameraRoute : ArgAppRoute<CameraArgs>("camera")

@Serializable
data class CameraArgs(val album: Album)