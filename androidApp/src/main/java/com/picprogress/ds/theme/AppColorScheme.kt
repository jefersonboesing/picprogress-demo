package com.picprogress.ds.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val colorScheme = lightColorScheme(
    primary = AppColors.Primary.Base,
    secondary = AppColors.Primary.Base
)

object AppColors {
    object Primary {
        val Base = Color(0xFF313265)
        val Darkest = Color(0xFF0A0A14)
        val Dark = Color(0xFF181932)
        val Medium = Color(0xFF313265)
        val Light = Color(0xFF9899B2)
        val Lightest = Color(0xFFEAEAF0)
    }

    object Additional {
        val Emerald = Color(0xFF95E2C7)
        val Ocean = Color(0xFF4978E1)
        val Flamingo = Color(0xFFE2A0D7)
        val Plum = Color(0xFF232562)
        val Lavender = Color(0xFFEDF1F4)
        val Tutorial = Color(0x4D000000)
    }

    object Neutral {
        object Low {
            val Darkest = Color(0xFF0D0D0D)
            val Dark = Color(0xFF323232)
            val Medium = Color(0XFF646464)
            val Light = Color(0xFF959597)
            val Lightest = Color(0xFFAEAEB0)
        }

        object High {
            val Darkest = Color(0xFFC7C7C9)
            val Dark = Color(0xFFE0E0E2)
            val Medium = Color(0xFFF2F2F4)
            val Light = Color(0xFFF7F7F9)
            val Lightest = Color(0xFFFFFFFF)
        }
    }
}