package com.picprogress.ds.components.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.picprogress.model.photo.Photo

@Composable
fun PhotoGrid(
    photos: List<Photo>,
    selectedPhotos: List<Photo>,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onPhotoClick: (Photo) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = 3),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        state = lazyGridState,
        contentPadding = PaddingValues(top = 16.dp, bottom = 64.dp)
    ) {
        items(photos, key = { it.uuid }) {
            PhotoItem(
                photo = it,
                selected = selectedPhotos.contains(it),
                onClick = onPhotoClick
            )
        }
    }
}