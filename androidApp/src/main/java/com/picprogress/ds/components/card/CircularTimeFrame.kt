@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.model.album.AlbumTheme

@Composable
fun FlowRowScope.CircularTimeFrame(
    text: String,
    isCompleted: Boolean,
    theme: AlbumTheme,
) {
    val boxHeight = if (text.isBlank()) 12.dp else 20.dp
    Box(
        modifier = Modifier
            .align(Alignment.Bottom)
            .weight(1f)
            .fillMaxWidth()
            .height(boxHeight)
            .background(
                color = if (isCompleted) theme.asSecondaryColorUI() else theme.asPrimaryTextColorUI().copy(alpha = 0.1f),
                shape = RoundedCornerShape(32.dp)
            )
            .clip(shape = RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.Center,
        content = {
            if (text.isNotBlank()) {
                TimeFrameLabel(
                    text = text,
                    color = if (isCompleted) theme.asSecondaryTextColorUI() else theme.asPrimaryTextColorUI(),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

@Composable
fun BoxScope.TimeFrameLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}