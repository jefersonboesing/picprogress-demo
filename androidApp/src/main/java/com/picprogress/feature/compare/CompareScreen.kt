package com.picprogress.feature.compare

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppFloatingActionButton
import com.picprogress.ds.components.photo.PhotoView
import com.picprogress.ds.components.AppIconTab
import com.picprogress.ds.components.AppTab
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.ds.screen.TutorialScreen
import com.picprogress.model.photo.ComparePhotos
import com.picprogress.model.photo.Photo
import com.picprogress.util.format
import com.picprogress.viewmodel.CompareViewModel.CompareMode
import com.picprogress.viewmodel.CompareViewModel.State

@Composable
fun CompareScreen(
    state: State,
    onBackClick: () -> Unit,
    onChangePhotosClick: () -> Unit,
    onCompareModeChange: (CompareMode) -> Unit,
    onOverModeHold: () -> Unit
) {
    Box {
        CompareScreenContent(
            state = state,
            onBackClick = onBackClick,
            onChangePhotosClick = onChangePhotosClick,
            onCompareModeChange = onCompareModeChange,
            onOverModeHold = onOverModeHold
        )
        if (state.compareMode == CompareMode.Over && !state.isCompareOverTutorialViewed) {
            TutorialScreen(
                icon = R.drawable.tutorial_hold_icon,
                message = stringResource(id = R.string.compare_screen_hold_to_compare)
            )
        }
    }
}

@Composable
private fun CompareScreenContent(
    state: State,
    onBackClick: () -> Unit,
    onChangePhotosClick: () -> Unit,
    onCompareModeChange: (CompareMode) -> Unit,
    onOverModeHold: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(AppColors.Neutral.High.Lightest)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompareTopBar(onBackClick = onBackClick)
        state.comparePhotos?.let { comparePhotos: ComparePhotos ->
            Box(modifier = Modifier.weight(1f).clipToBounds()) {
                AnimatedContent(targetState = state.compareMode, label = "") { compareMode ->
                    when (compareMode) {
                        CompareMode.SideBySide -> SideBySideMode(
                            photos = comparePhotos,
                            onChangePhotosClick = onChangePhotosClick
                        )

                        CompareMode.Over -> OverMode(
                            before = comparePhotos.beforePhoto,
                            after = comparePhotos.afterPhoto,
                            onOverModeHold = onOverModeHold
                        )
                    }
                }
            }
            CompareModesTab(
                currentMode = state.compareMode,
                modifier = Modifier.padding(bottom = 24.dp, top = 30.dp),
                onCompareModeChange = onCompareModeChange
            )
        } ?: run{
            PhotosComparePlaceholder(onAddPhotosClick = onChangePhotosClick)
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.compare_screen_no_photos_selected),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CompareModesTab(currentMode: CompareMode, modifier: Modifier = Modifier, onCompareModeChange: (CompareMode) -> Unit) {
    val modes = listOf(CompareMode.SideBySide, CompareMode.Over)
    AppTab(
        tabs = listOf(
            AppIconTab(icon = R.drawable.ic_side_by_side),
            AppIconTab(icon = R.drawable.ic_magic_wand)
        ),
        selectedIndex = modes.indexOf(currentMode),
        onTabClick = { onCompareModeChange(modes[it]) },
        modifier = modifier
    )
}

@Composable
private fun OverMode(
    before: Photo,
    after: Photo,
    onOverModeHold: () -> Unit,
) {
    var currentPhoto by remember { mutableStateOf(after) }
    Box {
        PhotoView(
            path = currentPhoto.photoPath.path,
            onPress = {
                currentPhoto = before
                onOverModeHold()
            },
            onRelease = { currentPhoto = after }
        )
        Image(
            painter = painterResource(id = R.drawable.app_logo_light),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }

}

@Composable
private fun SideBySideMode(
    photos: ComparePhotos,
    onChangePhotosClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3 / 4f),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PhotoView(
                    path = photos.beforePhoto.photoPath.path,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    onLeftClick = onChangePhotosClick,
                    onRightClick = onChangePhotosClick
                )
                PhotoView(
                    path = photos.afterPhoto.photoPath.path,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    onLeftClick = onChangePhotosClick,
                    onRightClick = onChangePhotosClick
                )
            }
            AppFloatingActionButton(icon = R.drawable.ic_rotate, onClick = onChangePhotosClick)
        }
        PhotoLabels(photos)
    }
}

@Composable
private fun PhotosComparePlaceholder(
    onAddPhotosClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3 / 4f),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PhotoPlaceholderBox(onAddPhotosClick)
                PhotoPlaceholderBox(onAddPhotosClick)
            }
            AppFloatingActionButton(icon = R.drawable.ic_plus, onClick = onAddPhotosClick)
        }
        PhotoLabels()
    }
}

@Composable
fun RowScope.PhotoPlaceholderBox(
    onAddPhotosClick: () -> Unit
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
    )
    Box(
        modifier = Modifier
            .clickable { onAddPhotosClick() }
            .weight(1f)
            .aspectRatio(3 / 4f)
            .drawBehind {
                drawRoundRect(
                    color = AppColors.Primary.Light,
                    style = stroke,
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = null,
            tint = AppColors.Primary.Light
        )
    }
}

@Composable
fun PhotoLabels(comparePhotos: ComparePhotos? = null) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PhotoLabel(stringResource(id = R.string.compare_screen_before), comparePhotos?.beforePhoto)
        PhotoLabel(stringResource(id = R.string.compare_screen_after), comparePhotos?.afterPhoto)
    }
}

@Composable
fun RowScope.PhotoLabel(title: String, photo: Photo?) {
    Column(
        modifier = Modifier
            .weight(1f)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = AppColors.Neutral.Low.Darkest,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = photo?.createdAt?.date?.format().orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.Neutral.Low.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CompareTopBar(
    onBackClick: () -> Unit,
) {
    AppTopBar(
        title = stringResource(id = R.string.compare_screen_title),
        titleAlign = TextAlign.Start,
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_arrow_left,
            onClick = onBackClick
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

