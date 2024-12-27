package com.picprogress.ds.components.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.model.album.Album
import com.picprogress.model.album.Frequency
import com.picprogress.model.photo.Photo
import com.picprogress.util.format
import com.picprogress.util.getDayNumber
import com.picprogress.util.getMonthNumber
import com.picprogress.util.getWeekNumber

@Composable
fun PhotoDate(album: Album, photo: Photo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = when (album.frequency) {
                Frequency.DAILY -> stringResource(id = R.string.photo_day_label, photo.createdAt.date.getDayNumber(album.createdAt.date))
                Frequency.WEEKLY -> stringResource(id = R.string.photo_week_label, photo.createdAt.date.getWeekNumber(album.createdAt.date))
                Frequency.MONTHLY -> stringResource(id = R.string.photo_month_label, photo.createdAt.date.getMonthNumber(album.createdAt.date))
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = photo.createdAt.date.format(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}