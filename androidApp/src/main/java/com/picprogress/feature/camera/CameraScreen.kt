package com.picprogress.feature.camera

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.picprogress.R
import com.picprogress.ds.components.AppButtonType
import com.picprogress.ds.components.AppToggleButton
import com.picprogress.ds.components.camera.AppCamera
import com.picprogress.ds.components.camera.rememberCameraControlState
import com.picprogress.ds.components.photo.PhotoView
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.ds.components.AppTopBarMode
import com.picprogress.model.camera.FlashMode
import com.picprogress.viewmodel.CameraViewModel.State
import java.io.File

@Composable
fun CameraScreen(
    state: State,
    onPhotoTaken: (File) -> Unit,
    onChangeFlashModeClick: () -> Unit,
    onChangeCameraLensClick: () -> Unit,
    onCloseClick: () -> Unit,
    onChangeCompareOpacityClick: () -> Unit,
    onCameraFailed: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraControlState = rememberCameraControlState(context = context, lifecycleOwner = lifecycleOwner)

    Column(
        modifier = Modifier
            .background(AppColors.Neutral.Low.Darkest)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CameraTopBar(
            state = state,
            onChangeFlashModeClick = onChangeFlashModeClick,
            onChangeCameraLensClick = onChangeCameraLensClick,
            onCloseClick = onCloseClick,
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
        ) {
            val (cameraView, photoView, toggleButton) = createRefs()
            AppCamera(
                state = cameraControlState,
                modifier = Modifier.constrainAs(cameraView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            if (state.isOpacityChecked && state.lastPhoto != null) {
                PhotoView(
                    path = state.lastPhoto?.photoPath?.path.orEmpty(),
                    modifier = Modifier
                        .constrainAs(photoView) {
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
                type = AppButtonType.Secondary,
                onCheckedChange = { onChangeCompareOpacityClick() },
                modifier = Modifier
                    .constrainAs(toggleButton) {
                        end.linkTo(cameraView.end)
                        bottom.linkTo(cameraView.bottom)
                    }
                    .padding(18.dp)
            )
        }
        Row(
            modifier = Modifier.height(142.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_shutter),
                contentDescription = null,
                modifier = Modifier.clickable { cameraControlState.takePicture(onPhotoTaken) }
            )
        }
    }

    LaunchedEffect(state.cameraLens) {
        val isSuccess = cameraControlState.setCameraLens(state.cameraLens)
        if (!isSuccess) onCameraFailed()
    }

    LaunchedEffect(state.flashMode) {
        cameraControlState.setFlashMode(state.flashMode)
    }
}

@Composable
private fun CameraTopBar(
    state: State,
    onChangeFlashModeClick: () -> Unit,
    onChangeCameraLensClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    AppTopBar(
        title = "",
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_close,
            onClick = onCloseClick
        ),
        endActions = listOf(
            AppTopBarAction.Icon(
                icon = state.flashMode.getIcon(),
                onClick = onChangeFlashModeClick
            ),
            AppTopBarAction.Icon(
                icon = R.drawable.ic_rotate,
                onClick = onChangeCameraLensClick
            )
        ),
        mode = AppTopBarMode.DARK,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@DrawableRes
private fun FlashMode.getIcon(): Int = when (this) {
    FlashMode.OFF -> R.drawable.ic_flash_disabled
    FlashMode.ON -> R.drawable.ic_flash
    FlashMode.AUTO -> R.drawable.ic_flash_a
}