package com.picprogress.feature.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.AppPrompt
import com.picprogress.ds.components.Option
import com.picprogress.ds.components.OptionsSheet
import com.picprogress.ds.components.photo.PhotoDate
import com.picprogress.ds.components.photo.PhotoView
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.viewmodel.PhotoViewModel.State

@Composable
fun PhotoScreen(
    state: State,
    onBackClick: () -> Unit,
    onCompareClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onEditDateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOptionsHidden: () -> Unit,
    onDeleteConfirmClick: () -> Unit,
    onConfirmationClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(AppColors.Neutral.High.Lightest)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoTopBar(onBackClick = onBackClick, onOptionsClick = onOptionsClick)
        Box(modifier = Modifier.weight(1f).clipToBounds()) {
            PhotoView(path = state.photo.photoPath.path)
        }
        Column(
            modifier = Modifier
                .background(AppColors.Neutral.High.Lightest)
                .fillMaxWidth()
                .height(142.dp)
        ) {
            PhotoDate(album = state.album, photo = state.photo)
            AppButton(
                text = stringResource(id = R.string.photo_screen_compare_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                startIcon = R.drawable.ic_compare,
                onClick = onCompareClick
            )
        }
    }

    if (state.isMoreOptionsVisible) {
        MoreOptions(
            onEditDateClick = onEditDateClick,
            onDeleteClick = onDeleteClick,
            onOptionsHidden = onOptionsHidden
        )
    }
    
    if (state.isDeleteConfirmationVisible) {
        AppPrompt(
            icon = R.drawable.ic_trash_dialog,
            title = stringResource(id = R.string.photo_screen_delete_confirmation_title),
            message = stringResource(id = R.string.photo_screen_delete_confirmation_message),
            positiveButtonText =  stringResource(id = R.string.delete),
            negativeButtonText = stringResource(id = R.string.cancel),
            onPositiveButtonClick = onDeleteConfirmClick,
            onNegativeButtonClick = onConfirmationClose,
            onDismiss = onConfirmationClose
        )
    }
}

@Composable
private fun PhotoTopBar(
    onBackClick: () -> Unit,
    onOptionsClick: () -> Unit,
) {
    AppTopBar(
        title = stringResource(id = R.string.photo_screen_title),
        titleAlign = TextAlign.Start,
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_arrow_left,
            onClick = onBackClick
        ),
        endActions = listOf(
            AppTopBarAction.Icon(
                icon = R.drawable.ic_more_horizontal,
                onClick = onOptionsClick
            )
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun MoreOptions(
    onEditDateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOptionsHidden: () -> Unit,
) {
    OptionsSheet(
        title = stringResource(id = R.string.photo_screen_more_options_title),
        options = listOf(
            Option(
                text = stringResource(id = R.string.photo_screen_more_options_edit_date),
                icon = R.drawable.ic_edit,
                onClick = onEditDateClick
            ),
            Option(
                text = stringResource(id = R.string.photo_screen_more_options_delete),
                icon = R.drawable.ic_trash,
                onClick = onDeleteClick
            )
        ),
        onDismiss = onOptionsHidden
    )
}