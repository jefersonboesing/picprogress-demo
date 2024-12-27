package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.theme.AppTheme

@Composable
fun AppFloatingActionButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = RoundedCornerShape(100.dp),
        containerColor = AppColors.Primary.Base
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AppFloatingActionButtonPreview() {
    AppTheme {
        AppFloatingActionButton(icon = R.drawable.ic_placeholder) {

        }
    }
}