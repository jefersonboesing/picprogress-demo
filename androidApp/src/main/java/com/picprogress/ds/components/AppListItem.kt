package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.picprogress.R

@Composable
fun AppListItem(
    label: String,
    startContent: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null,
    @DrawableRes endIcon: Int? = null,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        startContent?.invoke()
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.heightIn(min = 24.dp).weight(1f)
        )
        endContent?.invoke()
        if (endIcon != null) {
            Image(
                painter = painterResource(id = endIcon),
                contentDescription = null
            )
        }
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null
            )
        }
    }
}
