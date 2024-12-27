@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.Frequency


@Composable
fun AlbumCardContent(
    album: Album,
    summary: AlbumSummary,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = album.getMaxItemsInEachRow(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when (album.frequency) {
            Frequency.DAILY -> AlbumWeekDaysCardContent(album = album, summary = summary)
            Frequency.MONTHLY -> AlbumMonthsCardContent(album = album, summary = summary,)
            Frequency.WEEKLY -> AlbumWeeksCardContent(album = album, summary = summary)
        }
    }
}

private fun Album.getMaxItemsInEachRow() = when(this.frequency) {
    Frequency.MONTHLY -> 6
    Frequency.DAILY -> 7
    Frequency.WEEKLY -> 4
}