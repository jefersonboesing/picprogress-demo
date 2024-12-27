package com.picprogress.feature.preview

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.picprogress.ds.util.ActionEffect
import com.picprogress.ds.components.toast.AppToastHost
import com.picprogress.ds.components.toast.AppToastState
import com.picprogress.ds.components.toast.rememberAppToastState
import com.picprogress.model.album.Album
import com.picprogress.model.photo.PhotoPath
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.PreviewViewModel
import com.picprogress.viewmodel.PreviewViewModel.Action
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.previewScreen(
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(PreviewRoute) {
        val previewArgs = PreviewRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val viewModel: PreviewViewModel = koinViewModel(
            parameters = { parametersOf(previewArgs.photoPath, previewArgs.album) }
        )

        val toastState: AppToastState = rememberAppToastState(coroutineScope = rememberCoroutineScope())
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        PreviewScreen(
            state = uiState,
            onCloseClick = viewModel::onCloseClick,
            onChangeCompareOpacityClick = viewModel::onChangeCompareOpacityClick,
            onAddPhotoClick = viewModel::onAddPhotoClick,
            onBackToCameraClick = viewModel::onBackToCameraClick
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.Close -> onCloseClick()
                Action.GoBack -> onBackClick()
                is Action.ShowToast -> toastState.show(action.toastMessage, action.toastType)
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.initialize()
        }
        
        AppToastHost(state = toastState)
    }
}

fun NavController.navigateToPreview(album: Album, photoPath: PhotoPath) {
    navigate(PreviewRoute.build(PreviewArgs(album, photoPath)))
}

object PreviewRoute : ArgAppRoute<PreviewArgs>("camera-preview")

@Serializable
data class PreviewArgs(val album: Album, val photoPath: PhotoPath)