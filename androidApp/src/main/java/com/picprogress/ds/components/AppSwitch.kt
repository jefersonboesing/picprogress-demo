package com.picprogress.ds.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.picprogress.ds.theme.AppColors

@Composable
fun AppSwitch(
    checked: Boolean,
    onClick: () -> (Unit),
) {
    Switch(
        checked = checked,
        onCheckedChange = { onClick() },
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppColors.Neutral.High.Lightest,
            checkedTrackColor = AppColors.Primary.Medium,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = AppColors.Neutral.High.Lightest,
            uncheckedTrackColor = AppColors.Neutral.Low.Lightest,
            uncheckedBorderColor = Color.Transparent,
        ),
        thumbContent = {}
    )
}