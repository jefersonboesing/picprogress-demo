package com.picprogress.ds.util.navigation

import androidx.navigation.NamedNavArgument

interface AppRoute {
    val arguments: List<NamedNavArgument>
    val template: String
}