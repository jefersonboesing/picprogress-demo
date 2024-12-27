package com.picprogress.feature.compare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
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
import com.picprogress.viewmodel.CompareViewModel
import com.picprogress.viewmodel.CompareViewModel.Action
import com.picprogress.viewmodel.args.CompareArgs
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.compareScreen(
    onBackClick: () -> Unit,
    onOpenSelectToCompare: (Album, List<Photo>) -> Unit,
) {
    composable(CompareRoute) {
        val compareArgs = CompareRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val viewModel: CompareViewModel = koinViewModel(
            parameters = { parametersOf(compareArgs) }
        )
        val toastState = rememberAppToastState(coroutineScope = rememberCoroutineScope())
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        val photosToCompare: List<Photo> by it.savedStateHandle.getStateFlow<List<Photo>>(
            ScreenResultKeys.SelectedPhotosKey,
            emptyList()
        ).collectAsStateWithLifecycle()

        CompareScreen(
            state = uiState,
            onBackClick = viewModel::onBackClicked,
            onChangePhotosClick = viewModel::onChangePhotosClick,
            onCompareModeChange = viewModel::onCompareModeChange,
            onOverModeHold = viewModel::onOverModeHold
        )

        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) { action ->
            when (action) {
                Action.GoBack -> onBackClick()
                is Action.GoToPhotoSelection -> onOpenSelectToCompare(action.album, action.initialSelection)
                is Action.ShowToast -> toastState.show(action.toastMessage, action.toastType)
            }
        }

        LaunchedEffect(photosToCompare) {
            if (photosToCompare.size == 2) {
                viewModel.onComparePhotoSelectionChange(photosToCompare)
                it.savedStateHandle.remove<List<Photo>>(ScreenResultKeys.SelectedPhotosKey)
            }
        }
        AppToastHost(state = toastState)
    }
}

@Composable
fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember { mutableStateOf(null) }
    LaunchedEffect(key1 = reviewManager) {
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewInfo = it.result
            }
        }
    }
    return reviewInfo
}

fun NavController.navigateToCompare(album: Album, comparePhotos: ComparePhotos? = null) {
    navigate(CompareRoute.build(CompareArgs(album, comparePhotos)))
}

object CompareRoute : ArgAppRoute<CompareArgs>("compare")
