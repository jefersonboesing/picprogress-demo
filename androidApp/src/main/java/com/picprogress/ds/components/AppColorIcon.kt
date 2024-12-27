package com.picprogress.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorIcon(color: Color, size: Dp = 40.dp) {
    Box(
        modifier = Modifier
            .width(size)
            .height(size)
            .background(color = color, shape = RoundedCornerShape(size = 100.dp))
    )
}