@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.photo

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ComparePhotos(
    @JsonNames("beforePhoto")
    val beforePhoto: Photo,
    @JsonNames("afterPhoto")
    val afterPhoto: Photo
)