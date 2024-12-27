package com.picprogress.ds.components.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.picprogress.R
import com.picprogress.ds.theme.AppColors
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PhotoItem(
    photo: Photo,
    selected: Boolean,
    onClick: (Photo) -> Unit,
) {
    Box(modifier = Modifier
        .aspectRatio(1f)
        .fillMaxWidth()
        .background(
            color = Color(0x1A000000),
            shape = RoundedCornerShape(8.dp)
        )
        .clip(shape = RoundedCornerShape(8.dp))
        .clickable { onClick(photo) }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.photoPath.path)
                .size(200)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (selected) SelectionBox()
    }


}

@Composable
private fun SelectionBox() {
    Box(
        modifier = Modifier
            .background(Color(0x66FFFFFF))
            .fillMaxSize()
            .border(
                width = 2.dp,
                color = AppColors.Primary.Medium,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview
@Composable
private fun PhotoItemPreview() {
    PhotoItem(
        photo = Photo(uuid = "", photoPath = PhotoPath("test"), createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
        selected = true
    ) {

    }
}