@file:OptIn(ExperimentalMaterialNavigationApi::class)

package com.picprogress.feature.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.BottomSheetNavigatorSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.ds.util.ActionEffect
import com.picprogress.model.album.Album
import com.picprogress.ds.util.navigation.EmptyArgAppRoute
import com.picprogress.ds.util.navigation.composable
import com.picprogress.viewmodel.ActionState
import com.picprogress.viewmodel.HomeViewModel
import com.picprogress.viewmodel.HomeViewModel.Action
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
fun NavGraphBuilder.homeScreen(
    onNewAlbumClick: () -> Unit,
    onAlbumClick: (Album) -> Unit,
    bottomSheetNavigatorSheetState: BottomSheetNavigatorSheetState,
) {
    composable(HomeRoute) {
        val viewModel: HomeViewModel = koinViewModel()

        val actionState: ActionState<Action> by viewModel.actionState.collectAsStateWithLifecycle()
        val uiState: HomeViewModel.State by viewModel.uiState.collectAsStateWithLifecycle()

        HomeScreen(
            state = uiState,
            onAlbumClick = viewModel::onAlbumClick,
            onNewAlbumClick = viewModel::onNewAlbumClick,
            onContentModeChangeClick = viewModel::onContentModeChangeClick,
            onOpenFilterTypeClick = viewModel::onOpenFilterTypeClick,
            onOpenSortTypeClick = viewModel::onOpenSortTypeClick,
            onFilterTypeChange = viewModel::onFilterTypeChange,
            onSortTypeChange = viewModel::onSortTypeChange,
            onDialogDismiss = viewModel::onDialogDismiss
        )
        
        ActionEffect(actionState = actionState, onActionStateProcessed = viewModel::onActionStateProcessed) {action ->
            when(action) {
                is Action.GoToAlbum -> onAlbumClick(action.album)
                Action.GoToNewAlbum -> onNewAlbumClick()
            }
        }

        LaunchedEffect(bottomSheetNavigatorSheetState.isVisible) {
            viewModel.onRefresh()
        }
    }
}

object HomeRoute : EmptyArgAppRoute("home")