package com.picprogress.ds.util.navigation

import androidx.navigation.NamedNavArgument

open class EmptyArgAppRoute(private val name: String): AppRoute {
    override val arguments: List<NamedNavArgument> = listOf()
    override val template: String = name
}

fun EmptyArgAppRoute.build(): String {
    return template
}