package com.picprogress.util

import java.util.UUID

actual fun uuid(): String {
    return UUID.randomUUID().toString()
}