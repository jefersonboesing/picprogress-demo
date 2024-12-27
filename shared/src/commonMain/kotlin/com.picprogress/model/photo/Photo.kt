@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.photo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Photo(
    @JsonNames("id")
    val id: Long = 0,
    @JsonNames("uuid")
    val uuid: String,
    @JsonNames("photoPath")
    val photoPath: PhotoPath,
    @JsonNames("createdAt")
    val createdAt: LocalDateTime,
    @JsonNames("albumId")
    val albumId: Long = -1,
)