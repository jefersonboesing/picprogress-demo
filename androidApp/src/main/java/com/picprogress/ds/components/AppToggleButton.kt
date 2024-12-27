package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors

@Composable
fun AppToggleButton(
    @DrawableRes icon: Int,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.Primary,
) {
    val shape = RoundedCornerShape(100.dp)
    Box(
        modifier = modifier
            .background(type.backgroundColor(enabled, checked), shape)
            .size(48.dp)
            .clip(shape)
            .toggleable(
                value = checked,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                enabled = enabled,
                onValueChange = onCheckedChange
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = type.iconColor(enabled, checked)
        )
    }
}

private fun AppButtonType.iconColor(enabled: Boolean, checked: Boolean): Color {
    return when (this) {
        AppButtonType.Primary -> when {
            !enabled -> AppColors.Primary.Lightest
            checked -> AppColors.Neutral.High.Lightest
            else -> AppColors.Primary.Medium
        }
        AppButtonType.Secondary -> when {
            !enabled -> AppColors.Neutral.Low.Medium
            checked -> AppColors.Neutral.Low.Darkest
            else -> AppColors.Neutral.High.Lightest
        }
    }
}

private fun AppButtonType.backgroundColor(enabled: Boolean, checked: Boolean): Color {
    return when (this) {
        AppButtonType.Primary -> when {
            !enabled -> AppColors.Primary.Light
            checked -> AppColors.Primary.Medium
            else -> AppColors.Primary.Lightest
        }
        AppButtonType.Secondary -> when {
            !enabled -> AppColors.Neutral.Low.Dark
            checked -> AppColors.Neutral.High.Lightest
            else -> AppColors.Neutral.Low.Dark
        }
    }
}

@Preview
@Composable
fun AppToggleButtonPreview() {
    AppToggleButton(checked = true, enabled = false, onCheckedChange = {}, icon = R.drawable.ic_combine, type = AppButtonType.Secondary)
}