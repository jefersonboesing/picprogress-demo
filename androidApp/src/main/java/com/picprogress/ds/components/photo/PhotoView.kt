package com.picprogress.ds.components.photo

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PhotoView(
    path: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onLeftClick: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    onPress: (() -> Unit)? = null,
    onRelease: (() -> Unit)? = null
) {

    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(path)
            .crossfade(false)
            .build(),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3 / 4f)
            .wrapContentHeight()
            .clip(shape)
            .onGloballyPositioned { coordinates ->
                imageSize = coordinates.size
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        if (offset.x < imageSize.width / 2) {
                            onLeftClick?.invoke()
                        } else {
                            onRightClick?.invoke()
                        }
                    },
                    onPress = {
                        onPress?.invoke()
                        tryAwaitRelease()
                        onRelease?.invoke()
                    }
                )
            },
        contentScale = ContentScale.FillHeight
    )
}