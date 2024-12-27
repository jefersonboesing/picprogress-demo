package com.picprogress.local.storage

data class PhotoFile(
    val uuid: String,
    val path: String,
    val modifiedAt: Long
)