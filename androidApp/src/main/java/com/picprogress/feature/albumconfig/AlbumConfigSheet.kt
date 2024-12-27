package com.picprogress.feature.albumconfig

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.picprogress.LocalNavigationProvider
import com.picprogress.feature.albumconfig.frequency.albumConfigFrequencyScreen
import com.picprogress.feature.albumconfig.frequency.navigateToAlbumConfigFrequency
import com.picprogress.feature.albumconfig.main.AlbumConfigMainRoute
import com.picprogress.feature.albumconfig.main.albumConfigMainScreen
import com.picprogress.feature.albumconfig.theme.albumConfigThemeScreen
import com.picprogress.feature.albumconfig.theme.navigateToAlbumConfigTheme

@Composable
fun AlbumConfigSheet(
    albumConfigArgs: AlbumConfigArgs,
    onCloseClick: () -> Unit,
) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigationProvider provides navController) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .navigationBarsPadding()
                .imePadding()
                .padding(top = 12.dp)
        ) {
            NavHost(
                navController = navController,
                route = AlbumConfigRoute.template,
                startDestination = AlbumConfigMainRoute(albumConfigArgs).template,
            ) {
                albumConfigMainScreen(
                    defaultArgs = albumConfigArgs,
                    onClose = onCloseClick,
                    onGoToChangeThemeClick = {
                        navController.navigateToAlbumConfigTheme(it)
                    },
                    onGoToChangeFrequencyClick = {
                        navController.navigateToAlbumConfigFrequency(it)
                    }
                )
                albumConfigThemeScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
                albumConfigFrequencyScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
