package com.picprogress.ds.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
fun StatusBarEffect(isAppearanceLightStatusBarsForScreen: Boolean) {
    val context = LocalContext.current as Activity
    val window = WindowCompat.getInsetsController(context.window, context.window.decorView)
    DisposableEffect(Unit) {
        window.isAppearanceLightStatusBars = isAppearanceLightStatusBarsForScreen
        onDispose {
            window.isAppearanceLightStatusBars = !isAppearanceLightStatusBarsForScreen
        }
    }
}