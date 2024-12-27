package com.picprogress.ds.util.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument

open class ArgAppRoute<T>(private val name: String, private val defaultArg: String? = null) : AppRoute {
    private val JsonArg = "jsonArg"
    override val arguments = listOf(
        navArgument(JsonArg) {
            type = NavType.StringType
            if (defaultArg != null) defaultValue = defaultArg
        }
    )
    override val template: String = "$name/{$JsonArg}"
}

inline fun <reified T> ArgAppRoute<T>.build(args: T): String {
    return template.replace("{${arguments.first().name}}", args.toUriEncodedArg())
}

inline fun <reified T> ArgAppRoute<T>.getArgs(bundle: Bundle?): T? {
    return bundle?.getString(arguments.first().name).orEmpty().fromUriEncodedArgOrNull()
}