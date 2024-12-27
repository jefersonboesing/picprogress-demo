package com.picprogress.util

import platform.Foundation.NSUUID

actual fun uuid(): String {
    return NSUUID().UUIDString
}