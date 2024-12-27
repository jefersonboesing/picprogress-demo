package com.picprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    @ExperimentalMaterialNavigationApi
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        window.navigationBarColor = Color.Transparent.toArgb()
        setContent {
            App()
        }
    }

    private fun setupSplashScreen() {
        var isSplashVisible = true
        installSplashScreen().setKeepOnScreenCondition { isSplashVisible }
        lifecycleScope.launch {
            delay(1000)
            isSplashVisible = false
        }
    }
}