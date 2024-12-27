package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppTheme

@Composable
fun AppIconButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.Primary,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
        colors = when(type) {
            AppButtonType.Primary -> primaryButtonColors
            AppButtonType.Secondary -> secondaryButtonColors
        }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AppIconButtonPreview() {
    AppTheme {
        AppIconButton(
            icon = R.drawable.ic_placeholder
        ) {

        }
    }

}