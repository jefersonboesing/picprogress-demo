package com.picprogress.ds.components

import androidx.compose.material3.ButtonColors
import com.picprogress.ds.theme.AppColors

val primaryButtonColors = ButtonColors(
    containerColor = AppColors.Primary.Medium,
    contentColor = AppColors.Neutral.High.Lightest,
    disabledContainerColor = AppColors.Primary.Medium,
    disabledContentColor =  AppColors.Neutral.High.Lightest
)

val secondaryButtonColors = ButtonColors(
    containerColor = AppColors.Primary.Lightest,
    contentColor = AppColors.Primary.Dark,
    disabledContainerColor = AppColors.Primary.Lightest,
    disabledContentColor =   AppColors.Primary.Dark
)