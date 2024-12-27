@file:OptIn(ExperimentalPermissionsApi::class)

package com.picprogress.ds.components.camera

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.picprogress.ds.theme.AppColors

@Composable
fun AppCamera(state: AppCameraState, modifier: Modifier = Modifier) {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        AndroidView(
            modifier = modifier
                .background(AppColors.Neutral.Low.Darkest)
                .fillMaxWidth()
                .aspectRatio(3 / 4f),
            factory = { context -> state.createPreviewView(context) },
            update = { it.invalidate() }
        )
    } else {
        Box(
            modifier = modifier
                .background(AppColors.Neutral.Low.Darkest)
                .fillMaxWidth()
                .aspectRatio(3 / 4f)
        )
    }
    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}