package com.picprogress.feature.album

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.AppButtonType
import com.picprogress.ds.components.AppIconButton
import com.picprogress.ds.screen.EmptyScreen
import com.picprogress.ds.components.AppPrompt
import com.picprogress.ds.components.Option
import com.picprogress.ds.components.OptionsSheet
import com.picprogress.ds.components.photo.PhotoGrid
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.model.photo.Photo
import com.picprogress.viewmodel.AlbumViewModel.State

@Composable
fun AlbumScreen(
    state: State,
    onBackClick: () -> Unit,
    onAddPhotoClick: () -> Unit,
    onTakePictureClick: () -> Unit,
    onUploadFromGalleryClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    onSelectPhotosClick: () -> Unit,
    onEditAlbumClick: () -> Unit,
    onDeleteAlbumClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onOptionsHidden: () -> Unit,
    onDeletePhotosClick: () -> Unit,
    onCancelSelectionClick: () -> Unit,
    onCompareClick: () -> Unit,
    onDeletePhotosConfirmClick: () -> Unit,
    onDeleteAlbumConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AlbumTopBar(
            state = state,
            onBackClick = onBackClick,
            onOptionsClick = onMoreOptionsClick,
            onCancelSelectionClick = onCancelSelectionClick
        )

        if (state.isEmptyViewVisible) {
            EmptyScreen(
                image = R.drawable.ic_no_pictures,
                title = stringResource(id = R.string.album_screen_empty_title),
                description = stringResource(id = R.string.album_screen_empty_description),
                modifier = Modifier.weight(1f)
            )
        } else {
            Photos(
                photos = state.photos,
                selectedPhotos = state.selectedPhotos,
                modifier = Modifier.weight(1f),
                onPhotoClick = onPhotoClick
            )
        }

        AnimatedContent(targetState = state.isSelectionEnabled, label = "buttons") { selectionEnabled ->
            when (selectionEnabled) {
                true -> if (state.selectedPhotos.isNotEmpty()) {
                    SelectionButtons(
                        state = state,
                        onDeletePhotosClick = onDeletePhotosClick
                    )
                }

                false -> DefaultButtons(
                    state = state,
                    onAddPhotoClick = onAddPhotoClick,
                    onCompareClick = onCompareClick
                )
            }
        }
    }

    if (state.isAddPhotoOptionsVisible) {
        AddPhotoOptions(
            onTakePictureClick = onTakePictureClick,
            onUploadFromGalleryClick = onUploadFromGalleryClick,
            onOptionsHidden = onOptionsHidden
        )
    }

    if (state.isMoreOptionsVisible) {
        MoreOptions(
            state = state,
            onSelectPhotosClick = onSelectPhotosClick,
            onEditAlbumClick = onEditAlbumClick,
            onDeleteClick = onDeleteAlbumClick,
            onOptionsHidden = onOptionsHidden
        )

    }

    if (state.isPhotoDeleteConfirmationVisible) {
        AppPrompt(
            icon = R.drawable.ic_trash_dialog,
            title = stringResource(
                id = R.string.album_screen_photo_delete_confirmation_title, state.selectedPhotos.size.toString()
                    .padStart(2, '0')
            ),
            message = stringResource(id = R.string.album_screen_photo_delete_confirmation_description),
            positiveButtonText = stringResource(id = R.string.delete),
            negativeButtonText = stringResource(id = R.string.cancel),
            onPositiveButtonClick = onDeletePhotosConfirmClick,
            onNegativeButtonClick = onOptionsHidden,
            onDismiss = onOptionsHidden
        )
    }

    if (state.isAlbumDeleteConfirmationVisible) {
        AppPrompt(
            icon = R.drawable.ic_trash_dialog,
            title = stringResource(id = R.string.album_screen_album_delete_confirmation_title),
            message = stringResource(id = R.string.album_screen_album_delete_confirmation_description),
            positiveButtonText = stringResource(id = R.string.delete),
            negativeButtonText = stringResource(id = R.string.cancel),
            onPositiveButtonClick = onDeleteAlbumConfirmClick,
            onNegativeButtonClick = onOptionsHidden,
            onDismiss = onOptionsHidden
        )
    }
}

@Composable
private fun Photos(
    photos: List<Photo>,
    selectedPhotos: List<Photo>,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()
    Box(
        modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        PhotoGrid(
            photos = photos,
            selectedPhotos = selectedPhotos,
            onPhotoClick = onPhotoClick,
            lazyGridState = lazyGridState,
            modifier = Modifier.fillMaxSize()
        )
        AlbumSize(
            size = photos.size,
            isVisible = !lazyGridState.canScrollForward
        )
    }
}

@Composable
fun AddPhotoOptions(
    onTakePictureClick: () -> Unit,
    onUploadFromGalleryClick: () -> Unit,
    onOptionsHidden: () -> Unit,
) {
    OptionsSheet(
        title = stringResource(id = R.string.album_screen_add_photo_options_title),
        options = listOf(
            Option(
                text = stringResource(id = R.string.album_screen_add_photo_options_take_picture),
                icon = R.drawable.ic_camera,
                onClick = onTakePictureClick
            ),
            Option(
                text = stringResource(id = R.string.album_screen_add_photo_options_import),
                icon = R.drawable.ic_upload,
                onClick = onUploadFromGalleryClick
            )
        ),
        onDismiss = onOptionsHidden
    )
}

@Composable
fun MoreOptions(
    state: State,
    onSelectPhotosClick: () -> Unit,
    onEditAlbumClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOptionsHidden: () -> Unit,
) {
    val options = mutableListOf(
        Option(
            text = stringResource(id = R.string.album_screen_more_options_edit),
            icon = R.drawable.ic_edit,
            onClick = onEditAlbumClick
        ),
        Option(
            text = stringResource(id = R.string.album_screen_more_options_delete),
            icon = R.drawable.ic_trash,
            onClick = onDeleteClick
        )
    )
    if (state.photos.isNotEmpty()) {
        options.add(
            index = 0,
            element = Option(
                text = stringResource(id = R.string.album_screen_more_options_select),
                icon = R.drawable.ic_select,
                onClick = onSelectPhotosClick
            )
        )
    }
    OptionsSheet(
        title = stringResource(id = R.string.album_screen_more_options_title),
        options = options,
        onDismiss = onOptionsHidden
    )
}

@Composable
fun AlbumTopBar(
    state: State,
    onBackClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onCancelSelectionClick: () -> Unit,
) {
    val defaultActions = listOf(
        AppTopBarAction.Icon(icon = R.drawable.ic_more_horizontal, onClick = onOptionsClick)
    )
    val selectionAction = listOf(
        AppTopBarAction.Button(stringResource(id = R.string.cancel), onCancelSelectionClick)
    )
    AppTopBar(
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_arrow_left,
            onClick = onBackClick
        ),
        title = state.title,
        titleAlign = TextAlign.Start,
        endActions = if (state.isSelectionEnabled) selectionAction else defaultActions,
        contentPadding = PaddingValues(horizontal = 16.dp)
    )
}


@Composable
private fun SelectionButtons(
    state: State,
    onDeletePhotosClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pluralStringResource(
                id = R.plurals.album_screen_photos_selected,
                count = state.selectedPhotos.size,
                state.selectedPhotos.size
            ),
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.Neutral.Low.Darkest,
            modifier = Modifier.weight(1f).padding(start = 64.dp),
            textAlign = TextAlign.Center
        )
        AppIconButton(icon = R.drawable.ic_trash_2, onClick = onDeletePhotosClick, type = AppButtonType.Secondary)
    }
}

@Composable
private fun DefaultButtons(
    state: State,
    onAddPhotoClick: () -> Unit,
    onCompareClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppButton(
            text = stringResource(id = R.string.album_screen_add_button),
            modifier = Modifier.weight(1f),
            onClick = onAddPhotoClick
        )
        if (state.isCompareActionEnabled) {
            AppIconButton(icon = R.drawable.ic_compare, onClick = onCompareClick, type = AppButtonType.Secondary)
        }
    }
}


@Composable
fun AlbumSize(size: Int, isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = pluralStringResource(id = R.plurals.album_screen_photos_count, count = size, size),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}