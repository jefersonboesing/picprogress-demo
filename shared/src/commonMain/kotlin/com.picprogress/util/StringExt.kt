package com.picprogress.util

fun String.truncate(maxLength: Int): String {
    return if (length <= maxLength) this else take(maxLength) + "..."
}