@file:OptIn(ExperimentalFoundationApi::class)

package com.picprogress.feature.albumconfig.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppSwitch
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.card.AlbumCard
import com.picprogress.ds.components.card.asPrimaryColorUI
import com.picprogress.ds.components.ColorIcon
import com.picprogress.ds.components.AppListItem
import com.picprogress.ds.components.AppTextField
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.model.album.Frequency
import com.picprogress.viewmodel.AlbumConfigMainViewModel.State

@ExperimentalFoundationApi
@Composable
fun AlbumConfigMainScreen(
    uiState: State,
    onAlbumNameChange: (String) -> Unit,
    onAlbumNotesChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    onGoToChangeThemeClick: () -> Unit,
    onGoToChangeFrequencyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = AppColors.Neutral.High.Lightest)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTopBar(
            title = stringResource(id = if (uiState.isNew) R.string.configure_album_screen_title_new else R.string.configure_album_screen_title_edit),
            startAction = AppTopBarAction.Icon(
                icon = R.drawable.ic_arrow_down,
                onClick = onCloseClick
            )
        )

        AlbumConfigMainContent(
            uiState = uiState,
            onAlbumNameChange = onAlbumNameChange,
            onAlbumNotesChange = onAlbumNotesChange,
            onGoToChangeThemeClick = onGoToChangeThemeClick,
            onGoToChangeFrequencyClick = onGoToChangeFrequencyClick
        )

        AppButton(
            text = stringResource(id =  if (uiState.isNew) R.string.create else R.string.save),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            onSaveClick()
        }
    }
}

@Composable
private fun ColumnScope.AlbumConfigMainContent(
    uiState: State,
    onAlbumNameChange: (String) -> Unit,
    onAlbumNotesChange: (String) -> Unit,
    onGoToChangeThemeClick: () -> Unit,
    onGoToChangeFrequencyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumCard(
            album = uiState.album,
            modifier = Modifier.padding(16.dp),
            summary = uiState.summary,
            onClick = {}
        )

        AppTextField(
            label = stringResource(id = R.string.configure_album_name_label),
            value = uiState.album.title,
            onValueChange = onAlbumNameChange,
            placeholderText = stringResource(id = R.string.configure_album_name_placeholder),
            errorText = if (uiState.isTitleInvalid) stringResource(id = R.string.configure_album_name_error) else null,
            singleLine = true,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )

        AppTextField(
            label = stringResource(id = R.string.configure_album_notes_label),
            value = uiState.album.notes,
            onValueChange = onAlbumNotesChange,
            placeholderText = stringResource(id = R.string.configure_album_notes_placeholder),
            minLines = 5,
            maxLines = 5,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )

        Settings(
            uiState = uiState,
            onGoToChangeThemeClick = onGoToChangeThemeClick,
            onGoToFrequencyClick = onGoToChangeFrequencyClick
        )
    }
}

@Composable
private fun Settings(
    uiState: State,
    onGoToChangeThemeClick: () -> Unit,
    onGoToFrequencyClick: () -> Unit,
) {
    Text(
        text = stringResource(id = R.string.configure_album_settings_title),
        style = MaterialTheme.typography.titleSmall,
        color = Color(0xFF959597),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.size(8.dp))
    if (uiState.isNew) {
        AppListItem(
            label = stringResource(id = R.string.configure_album_photo_frequency_label),
            endContent = {
                Text(
                    text = uiState.album.frequency.toDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF646464)
                )
            },
            endIcon = R.drawable.ic_arrow_right,
            onClick = onGoToFrequencyClick
        )
    }
    AppListItem(
        label = stringResource(id = R.string.configure_album_background_color_label),
        endContent = { ColorIcon(color = uiState.album.theme.asPrimaryColorUI(), size = 32.dp) },
        endIcon = R.drawable.ic_arrow_right,
        onClick = onGoToChangeThemeClick
    )
}

@Composable
fun Frequency.toDescription(): String {
    return when (this) {
        Frequency.DAILY -> stringResource(id = R.string.frequency_daily)
        Frequency.WEEKLY -> stringResource(id = R.string.frequency_weekly)
        Frequency.MONTHLY -> stringResource(id = R.string.frequency_monthly)
    }
}