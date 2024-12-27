package com.picprogress.feature.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.AppButtonType
import com.picprogress.ds.components.AppIconButton
import com.picprogress.ds.components.AppToggleButton
import com.picprogress.ds.components.photo.PhotoView
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.viewmodel.PreviewViewModel.State

@Composable
fun PreviewScreen(
    state: State,
    onCloseClick: () -> Unit,
    onChangeCompareOpacityClick: () -> Unit,
    onAddPhotoClick: () -> Unit,
    onBackToCameraClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(AppColors.Neutral.High.Lightest)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PreviewTopBar(onCloseClick = onCloseClick)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
        ) {
            val (currentPhotoView, beforePhotoView, toggleButton) = createRefs()
            PhotoView(
                path = state.photoPath.path,
                modifier = Modifier.constrainAs(currentPhotoView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            if (state.isOpacityChecked && state.lastPhoto != null) {
                PhotoView(
                    path = state.lastPhoto?.photoPath?.path.orEmpty(),
                    modifier = Modifier
                        .constrainAs(beforePhotoView) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .alpha(0.4f)
                )
            }
            AppToggleButton(
                icon = if (state.isOpacityChecked) R.drawable.ic_subtract else R.drawable.ic_combine,
                checked = state.isOpacityChecked,
                enabled = state.lastPhoto != null,
                modifier = Modifier
                    .constrainAs(toggleButton) {
                        end.linkTo(currentPhotoView.end)
                        bottom.linkTo(currentPhotoView.bottom)
                    }
                    .padding(18.dp),
                type = AppButtonType.Primary,
                onCheckedChange = { onChangeCompareOpacityClick() },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(142.dp)
                .padding(horizontal = 16.dp).padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            AppIconButton(
                icon = R.drawable.ic_arrow_left,
                onClick = onBackToCameraClick,
                type = AppButtonType.Secondary
            )
            AppButton(
                text = stringResource(id = R.string.preview_screen_add_button),
                modifier = Modifier.weight(1f),
                onClick = onAddPhotoClick
            )
        }

    }
}

@Composable
private fun PreviewTopBar(
    onCloseClick: () -> Unit,
) {
    AppTopBar(
        title = "",
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_close,
            onClick = onCloseClick
        ),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}