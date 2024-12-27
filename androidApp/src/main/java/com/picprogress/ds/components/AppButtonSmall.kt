package com.picprogress.ds.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.ds.theme.AppTheme

@Composable
fun AppButtonSmall(
    text: String,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.Secondary,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        modifier = modifier.height(32.dp),
        enabled = enabled,
        colors = when (type) {
            AppButtonType.Primary -> primaryButtonColors
            AppButtonType.Secondary -> secondaryButtonColors
        }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
private fun AppButtonSmallPreview() {
    AppTheme {
        AppButtonSmall(
            text = "Cancel"
        ) {

        }
    }

}