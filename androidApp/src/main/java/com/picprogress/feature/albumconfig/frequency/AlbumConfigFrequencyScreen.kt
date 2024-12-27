package com.picprogress.feature.albumconfig.frequency

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
import com.picprogress.ds.components.AppListItem
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.feature.albumconfig.main.toDescription
import com.picprogress.model.album.Frequency
import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel.State

@Composable
fun AlbumConfigFrequencyScreen(
    state: State,
    onBackClick: () -> Unit,
    onSelectedFrequencyChange: (Frequency) -> Unit,
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
            title = stringResource(id = R.string.configure_album_frequency_screen_title),
            startAction = AppTopBarAction.Icon(
                icon = R.drawable.ic_arrow_left,
                onClick = onBackClick
            )
        )

        state.frequencies.forEach { frequency ->
            AppListItem(
                label = frequency.toDescription(),
                selected = state.selectedFrequency == frequency,
                onClick = { onSelectedFrequencyChange(frequency) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = stringResource(id = R.string.configure_album_frequency_screen_apply_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            onApplyClick()
        }
    }
}
