@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.preferences

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Preferences(
    @JsonNames("isCompareOverTutorialViewed")
    val isCompareOverTutorialViewed: Boolean = false
)