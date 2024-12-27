package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.theme.AppTheme

@Composable
fun AppFilterButton(
    text: String,
    @DrawableRes icon: Int,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.0.dp),
        enabled = enabled,
        modifier = Modifier.requiredHeight(28.dp).widthIn(min = 46.dp),
        colors = ButtonColors(
            containerColor = AppColors.Neutral.High.Light,
            contentColor = AppColors.Neutral.Low.Medium,
            disabledContainerColor = Color(0xFF646464),
            disabledContentColor = Color(0xFF646464)
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier.padding(0.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = AppColors.Neutral.Low.Medium
        )
        Spacer(modifier = Modifier.size(4.dp))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun AppChipButtonPreview() {
    AppTheme {
        AppFilterButton(
            text = "All",
            icon = R.drawable.ic_arrow_up_down
        ) {

        }
    }

}