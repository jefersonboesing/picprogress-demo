package com.picprogress.util

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

object Strings {
    fun get(id: String): String = id.localized()
    fun get(id: String, quantity: Int) = id.localized(quantity)
    fun format(id: String, vararg formatArgs: Any) = id.localized(*formatArgs)
}

fun String.localized(): String {
    val localizedString = NSBundle.mainBundle.localizedStringForKey(this, this, null)
    if (localizedString != this) return localizedString
    return this
}

fun String.localized(vararg arguments: Any?): String {
    val format = localized()
    return when (arguments.size) {
        0 -> NSString.stringWithFormat(format)
        1 -> NSString.stringWithFormat(format, arguments[0])
        2 -> NSString.stringWithFormat(format, arguments[0], arguments[1])
        3 -> NSString.stringWithFormat(format, arguments[0], arguments[1], arguments[2])
        else -> error("Too many arguments.")
    }
}