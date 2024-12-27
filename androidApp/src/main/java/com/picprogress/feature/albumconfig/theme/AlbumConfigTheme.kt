package com.picprogress.feature.albumconfig.theme

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.picprogress.ds.util.ActionEffect
import com.picprogress.koinSharedViewModel
import com.picprogress.model.album.AlbumTheme
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.AlbumConfigThemeViewModel
import com.picprogress.viewmodel.AlbumConfigThemeViewModel.Action
import com.picprogress.viewmodel.AlbumConfigViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.albumConfigThemeScreen(
    onBack: () -> Unit,
) {
    composable(appRoute = AlbumConfigThemeRoute) {
        val albumConfigThemeArg = AlbumConfigThemeRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val sharedViewModel: AlbumConfigViewModel = it.koinSharedViewModel()
        val viewModel: AlbumConfigThemeViewModel = koinViewModel(
            parameters = { parametersOf(albumConfigThemeArg.currentTheme) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        AlbumConfigThemeScreen(
            state = uiState,
            onBackClick = viewModel::onBackClick,
            onSelectedThemeChange = viewModel::onSelectedAlbumThemeChange,
            onApplyClick = viewModel::onApplyClicked
        )

        ActionEffect(
            actionState = actionState,
            onActionStateProcessed = viewModel::onActionStateProcessed
        ) { action ->
            when (action) {
                is Action.ApplyAlbumTheme -> {
                    sharedViewModel.albumTheme = action.theme
                    onBack()
                }
                Action.GoBack -> onBack()
            }
        }

    }
}

fun NavController.navigateToAlbumConfigTheme(currentTheme: AlbumTheme) {
    navigate(AlbumConfigThemeRoute.build(AlbumConfigThemeArg(currentTheme)))
}

object AlbumConfigThemeRoute: ArgAppRoute<AlbumConfigThemeArg>("album-config-theme")

@Serializable
data class AlbumConfigThemeArg(val currentTheme: AlbumTheme)