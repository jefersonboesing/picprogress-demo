package com.picprogress.feature.photoselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.screen.EmptyScreen
import com.picprogress.ds.components.photo.PhotoGrid
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.model.photo.Photo
import com.picprogress.viewmodel.PhotoSelectionViewModel.State

@Composable
fun PhotoSelectionSheet(
    state: State,
    onCloseClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onApplyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = AppColors.Neutral.High.Lightest)
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTopBar(
            title = stringResource(id = R.string.photo_config_selection_title),
            startAction = AppTopBarAction.Icon(
                icon = R.drawable.ic_arrow_down,
                onClick = onCloseClick
            ),
        )
        if (state.photos.isEmpty()) {
            EmptyScreen(
                image = R.drawable.ic_no_pictures,
                title = stringResource(id = R.string.photo_config_selection_empty_title),
                description = stringResource(id = R.string.photo_config_selection_empty_message),
                modifier = Modifier.weight(1f)
            )
        } else {
            PhotoGrid(
                photos = state.photos,
                selectedPhotos = state.selectedPhotos,
                onPhotoClick = onPhotoClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }
        if (state.isReady) {
            AppButton(
                text = stringResource(
                    id = R.string.photo_config_selection_apply_button
                ),
                onClick = onApplyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else if (state.photos.isNotEmpty()) {
            Text(
                text = pluralStringResource(id = R.plurals.photo_config_selection_required_label, state.minRequired, state.minRequired),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}