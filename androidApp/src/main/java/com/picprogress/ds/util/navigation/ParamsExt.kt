package com.picprogress.ds.util.navigation

import android.net.Uri
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.toUriEncodedArg(): String {
    return Uri.encode(Json.encodeToString(this))
}

inline fun <reified T> String.fromUriEncodedArg(): T {
    return Json.decodeFromString(Uri.decode(this))
}

inline fun <reified T> String.fromUriEncodedArgOrNull(): T? = try {
    fromUriEncodedArg<T>()
} catch (ex: Exception) {
    null
}