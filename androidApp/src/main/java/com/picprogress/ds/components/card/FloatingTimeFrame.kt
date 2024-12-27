@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picprogress.model.album.AlbumTheme

@Composable
fun FlowRowScope.FloatingTimeFrame(
    text: String,
    isCurrent: Boolean,
    isCompleted: Boolean,
    theme: AlbumTheme,
    textAlignment: Alignment = Alignment.TopCenter
) {
    val maxHeight: Dp = 44.dp
    val minHeight: Dp = 12.dp
    val textColor = if (isCompleted && isCurrent) theme.asSecondaryTextColorUI() else theme.asPrimaryTextColorUI()
    var backgroundHeight: Dp by remember { mutableStateOf(minHeight) }
    var textColorAlpha: Float by remember { mutableStateOf(1f) }
    val backgroundColor = if (isCompleted) theme.asSecondaryColorUI() else theme.asPrimaryTextColorUI().copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .weight(1f)
            .requiredHeight(maxHeight)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = backgroundHeight)
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .clip(shape = RoundedCornerShape(8.dp))
        )
        TimeFrameLabel(
            text = text,
            color = textColor.copy(alpha = textColorAlpha),
            modifier = Modifier.align(textAlignment).padding(top = 13.dp)
        )
    }

    LaunchedEffect(isCurrent) {
        val targetHeight = if (isCurrent) maxHeight.value else minHeight.value
        animate(
            initialValue = minHeight.value,
            targetValue = targetHeight,
            animationSpec = tween(durationMillis = 800)
        ) { value, _ ->
            backgroundHeight = Dp(value)
            textColorAlpha = (value / targetHeight).coerceAtMost(1f)
        }
    }
}