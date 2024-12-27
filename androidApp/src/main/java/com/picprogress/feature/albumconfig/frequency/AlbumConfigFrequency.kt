package com.picprogress.feature.albumconfig.frequency

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.picprogress.ds.util.ActionEffect
import com.picprogress.koinSharedViewModel
import com.picprogress.model.album.Frequency
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.composable
import com.picprogress.ds.util.navigation.getArgs
import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel
import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel.Action
import com.picprogress.viewmodel.AlbumConfigViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.albumConfigFrequencyScreen(
    onBack: () -> Unit,
) {
    composable(AlbumConfigFrequencyRoute) {
        val args = AlbumConfigFrequencyRoute.getArgs(it.arguments) ?: throw IllegalStateException()
        val sharedViewModel: AlbumConfigViewModel = it.koinSharedViewModel()
        val viewModel: AlbumConfigFrequencyViewModel = koinViewModel(
            parameters = { parametersOf(args.currentFrequency) }
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val actionState by viewModel.actionState.collectAsStateWithLifecycle()

        AlbumConfigFrequencyScreen(
            state = uiState,
            onBackClick = viewModel::onBackClick,
            onSelectedFrequencyChange = viewModel::onSelectedFrequencyChange,
            onApplyClick = viewModel::onApplyClicked
        )

        ActionEffect(
            actionState = actionState,
            onActionStateProcessed = viewModel::onActionStateProcessed
        ) { action ->
            when (action) {
                is Action.ApplyAlbumFrequency -> {
                    sharedViewModel.frequency = action.frequency
                    onBack()
                }
                Action.GoBack -> onBack()
            }
        }
    }
}

fun NavController.navigateToAlbumConfigFrequency(currentFrequency: Frequency) {
    navigate(AlbumConfigFrequencyRoute.build(AlbumConfigFrequencyArg(currentFrequency)))
}

object AlbumConfigFrequencyRoute: ArgAppRoute<AlbumConfigFrequencyArg>("album-config-frequency")

@Serializable
data class AlbumConfigFrequencyArg(val currentFrequency: Frequency)