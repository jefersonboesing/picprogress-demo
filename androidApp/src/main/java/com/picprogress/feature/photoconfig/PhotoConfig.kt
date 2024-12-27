package com.picprogress.feature.photoconfig

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.ds.util.ActionEffect
import com.picprogress.model.photo.Photo
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.bottomSheet
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.PhotoConfigViewModel
import com.picprogress.viewmodel.PhotoConfigViewModel.Action
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.photoConfigSheet(
    onCloseClick: () -> Unit
) {
    bottomSheet(PhotoConfigRoute) {
        val photoConfigArgs = PhotoConfigRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val viewModel: PhotoConfigViewModel = koinViewModel(
            parameters = { parametersOf(photoConfigArgs.photo) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        PhotoConfigSheet(
            state = uiState,
            onCloseClick = onCloseClick,
            onDateChange = viewModel::onDateChanged,
            onSaveClick = viewModel::onSaveClick
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.Close -> onCloseClick()
            }
        }
    }
}

fun NavController.navigateToPhotoConfig(photo: Photo) {
    navigate(PhotoConfigRoute.build(PhotoConfigArgs(photo)))
}

object PhotoConfigRoute : ArgAppRoute<PhotoConfigArgs>("photo-config")

@Serializable
data class PhotoConfigArgs(val photo: Photo)