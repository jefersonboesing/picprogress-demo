package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.Primary,
    @DrawableRes startIcon: Int? = null,
    @DrawableRes endIcon: Int? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        modifier = modifier.height(56.dp),
        enabled = enabled,
        colors = when (type) {
            AppButtonType.Primary -> primaryButtonColors
            AppButtonType.Secondary -> secondaryButtonColors
        }
    ) {
        if (startIcon != null) {
            Icon(
                painter = painterResource(id = startIcon),
                contentDescription = null
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        if (endIcon != null) {
            Icon(
                painter = painterResource(id = endIcon),
                contentDescription = null
            )
        }
    }
}
