package com.picprogress.feature.albumconfig.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.card.asPrimaryColorUI
import com.picprogress.ds.components.ColorIcon
import com.picprogress.ds.components.AppListItem
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.model.album.AlbumTheme
import com.picprogress.viewmodel.AlbumConfigThemeViewModel.State

@Composable
fun AlbumConfigThemeScreen(
    state: State,
    onBackClick: () -> Unit,
    onSelectedThemeChange: (AlbumTheme) -> Unit,
    onApplyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = AppColors.Neutral.High.Lightest)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppTopBar(
            title = stringResource(id = R.string.configure_album_background_screen_title),
            startAction = AppTopBarAction.Icon(
                icon = R.drawable.ic_arrow_left,
                onClick = onBackClick
            )
        )

        state.theme.forEach { theme ->
            AppListItem(
                label = theme.name,
                startContent = { ColorIcon(color = theme.asPrimaryColorUI(), size = 40.dp) },
                selected = state.selectedTheme == theme,
                onClick = { onSelectedThemeChange(theme) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = stringResource(id = R.string.configure_album_background_screen_apply_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            onApplyClick()
        }
    }
}
