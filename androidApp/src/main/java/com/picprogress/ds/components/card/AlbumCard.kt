package com.picprogress.ds.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.TimeFrame
import com.picprogress.model.album.TimeRemaining
import com.picprogress.util.truncate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

@Composable
fun AlbumCard(
    album: Album,
    modifier: Modifier = Modifier,
    isSmallMode: Boolean = false,
    summary: AlbumSummary,
    onClick: () -> Unit,
) {
    val cardHeight = 162.dp
    val cardCorner = 16.dp
    val cardPadding = 16.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(cardHeight)
            .background(
                color = album.theme.asPrimaryColorUI(),
                shape = RoundedCornerShape(cardCorner)
            )
            .clip(shape = RoundedCornerShape(cardCorner))
            .clickable { onClick() }
            .padding(cardPadding)
    ) {
        AlbumCardHeader(
            album = album,
            summary = summary,
            isSmallMode = isSmallMode
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isSmallMode) {
            AlbumCardContentSmall(
                album = album,
                summary = summary
            )
        } else {
            AlbumCardContent(
                album = album,
                summary = summary
            )
        }
    }
}

@Composable
fun AlbumCardHeader(
    album: Album,
    summary: AlbumSummary,
    isSmallMode: Boolean,
) {
    Column {
        Row {
            Text(
                text = album.title.truncate(18),
                style = MaterialTheme.typography.titleLarge,
                color = album.theme.asPrimaryTextColorUI(),
                modifier = Modifier.weight(1f)
            )
            if (!isSmallMode) {
                Status(album = album, summary = summary)
            }
        }
        Text(
            text = when {
                summary.albumSize == 0 -> stringResource(id = R.string.photos_size_label_zero)
                summary.albumSize > 0 -> pluralStringResource(
                    id = R.plurals.photos_size_label,
                    count = summary.albumSize,
                    summary.albumSize
                )
                else -> ""
            },
            style = MaterialTheme.typography.bodySmall,
            color = album.theme.asPrimaryTextColorUI()
        )
    }
}

@Composable
private fun Status(album: Album, summary: AlbumSummary) {
    if (summary.isUpdated) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.album_card_updated_label),
                style = MaterialTheme.typography.bodySmall,
                color = album.theme.asPrimaryTextColorUI()
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_updated_check),
                contentDescription = null,
                tint = album.theme.asPrimaryTextColorUI()
            )
        }
    } else {
        Text(
            text = summary.timeRemaining?.toText().orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = album.theme.asPrimaryTextColorUI()
        )
    }
}

fun AlbumTheme.asSecondaryColorUI() = Color(secondaryColor)
fun AlbumTheme.asSecondaryTextColorUI() = Color(secondaryTextColor)
fun AlbumTheme.asPrimaryColorUI() = Color(primaryColor)
fun AlbumTheme.asPrimaryTextColorUI() = Color(primaryTextColor)

@Composable
private fun TimeRemaining.toText(): String {
    return when(this) {
        is TimeRemaining.Days -> pluralStringResource(id = R.plurals.album_card_days_remaining, count = days.toInt(), days.toInt())
        is TimeRemaining.Hours -> pluralStringResource(id = R.plurals.album_card_hours_remaining, count = hours.toInt(), hours.toInt())
        is TimeRemaining.Minutes -> pluralStringResource(id = R.plurals.album_card_minutes_remaining, count = minutes.toInt(), minutes.toInt())
    }
}