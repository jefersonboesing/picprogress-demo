@file:OptIn(ExperimentalFoundationApi::class)

package com.picprogress.feature.albumconfig.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.picprogress.ds.util.ActionEffect
import com.picprogress.feature.albumconfig.AlbumConfigArgs
import com.picprogress.koinSharedViewModel
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.Frequency
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.ds.util.navigation.toUriEncodedArg
import com.picprogress.viewmodel.AlbumConfigMainViewModel
import com.picprogress.viewmodel.AlbumConfigMainViewModel.Action
import com.picprogress.viewmodel.AlbumConfigViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.albumConfigMainScreen(
    onClose: () -> Unit,
    onGoToChangeThemeClick: (AlbumTheme) -> Unit,
    onGoToChangeFrequencyClick: (Frequency) -> Unit,
    defaultArgs: AlbumConfigArgs
) {
    val route = AlbumConfigMainRoute(defaultArgs)
    composable(
        appRoute = route,
        enterTransition = { slideInHorizontally(tween(500), initialOffsetX = { -it }) },
        exitTransition = { slideOutHorizontally(tween(500), targetOffsetX = { -it }) }
    ) {
        val albumConfigArgs = route.getArgs(it.arguments)
        val sharedViewModel: AlbumConfigViewModel = it.koinSharedViewModel()
        val viewModel: AlbumConfigMainViewModel = koinViewModel(
            parameters = { parametersOf(albumConfigArgs?.album) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        AlbumConfigMainScreen(
            uiState = uiState,
            onAlbumNameChange = viewModel::onTitleChange,
            onAlbumNotesChange = viewModel::onNotesChange,
            onCloseClick = viewModel::onCloseClick,
            onSaveClick = viewModel::onSaveAlbumClick,
            onGoToChangeThemeClick = viewModel::onGoToChangeThemeClick,
            onGoToChangeFrequencyClick = viewModel::onGoToChangeFrequencyClick
        )

        ActionEffect(
            actionState = actionState,
            onActionStateProcessed = viewModel::onActionStateProcessed
        ) { action ->
            when (action) {
                Action.Close -> onClose()
                is Action.GoToChangeTheme -> onGoToChangeThemeClick(action.current)
                is Action.GoToChangeFrequency -> onGoToChangeFrequencyClick(action.current)
            }
        }

        LaunchedEffect(Unit) {
            sharedViewModel.albumTheme?.let { viewModel.onAlbumThemeChange(it) }
            sharedViewModel.frequency?.let { viewModel.onAlbumFrequencyChange(it) }
        }

        BackHandler {
            viewModel.onCloseClick()
        }
    }
}

class AlbumConfigMainRoute(defaultArgs: AlbumConfigArgs) : ArgAppRoute<AlbumConfigArgs>("album-config-main", defaultArgs.toUriEncodedArg())