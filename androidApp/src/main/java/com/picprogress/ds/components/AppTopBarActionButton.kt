package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.picprogress.R

@Composable
fun AppTopBarIcon(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = true
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AppTopBarButtonPreview() {
    AppTopBarIcon(icon = R.drawable.ic_placeholder) {
        
    }
}