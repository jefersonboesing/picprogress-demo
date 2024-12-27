package com.picprogress.ds.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.ds.theme.AppColors

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .background(AppColors.Neutral.High.Lightest)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeCap = StrokeCap.Round,
            strokeWidth = 8.dp,
            color = AppColors.Primary.Medium,
            trackColor = AppColors.Neutral.High.Lightest,
            modifier = Modifier.size(64.dp)
        )
    }
}


@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}