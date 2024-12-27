package com.picprogress.util

sealed class AppColor(val color: Long) {
    sealed class Primary(color: Long): AppColor(color) {
        data object Base: Primary(0xFF313265)
        data object Darkest: Primary(0xFF0A0A14)
        data object Dark: Primary(0xFF181932)
        data object Medium: Primary(0xFF313265)
        data object Light: Primary(0xFF9899B2)
        data object Lightest: Primary(0xFFEAEAF0)
    }

    sealed class Additional(color: Long): AppColor(color) {
        data object Emerald: Additional(0xFF95E2C7)
        data object Ocean: Additional(0xFF4978E1)
        data object Flamingo: Additional(0xFFE2A0D7)
        data object Plum: Additional(0xFF232562)
        data object Lavender: Additional(0xFFEDF1F4)
        data object Tutorial: Additional(0x4D000000)
    }

    sealed class Neutral(color: Long): AppColor(color) {
        sealed class Low(color: Long): Neutral(color) {
            data object Darkest: Low(0xFF0D0D0D)
            data object Dark: Low(0xFF323232)
            data object Medium: Low(0XFF646464)
            data object Light: Low(0xFF959597)
            data object Lightest: Low(0xFFAEAEB0)
        }

        sealed class High(color: Long): Neutral(color) {
            data object Darkest: High(0xFFC7C7C9)
            data object Dark: High(0xFFE0E0E2)
            data object Medium: High(0xFFF2F2F4)
            data object Light: High(0xFFF7F7F9)
            data object Lightest: High(0xFFFFFFFF)
        }
    }

    sealed class Status(color: Long): AppColor(color) {
        sealed class Critical(color: Long): Status(color) {
            data object Medium: Critical(0xFFF31527)
        }
    }
}