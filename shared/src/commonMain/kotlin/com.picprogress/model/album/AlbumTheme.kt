@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.album

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Parcelize
@Serializable
sealed class AlbumTheme(
    @JsonNames("primaryColor")
    val name: String,
    @JsonNames("primaryColor")
    val primaryColor: Long,
    @JsonNames("secondaryColor")
    val secondaryColor: Long,
    @JsonNames("primaryTextColor")
    val primaryTextColor: Long,
    @JsonNames("secondaryTextColor")
    val secondaryTextColor: Long,
) : Parcelable {

    val id: Long = primaryColor

    @Serializable
    object Emerald : AlbumTheme(
        name = "Emerald",
        primaryColor = 0xFF95E2C7,
        secondaryColor = 0xFF313265,
        primaryTextColor = 0xFF0D0D0D,
        secondaryTextColor = 0xFFFFFFFF
    )

    @Serializable
    object Plum : AlbumTheme(
        name = "Plum",
        primaryColor = 0xFF232562,
        secondaryColor = 0xFFEAEAF0,
        primaryTextColor = 0xFFFFFFFF,
        secondaryTextColor = 0xFF0D0D0D
    )

    @Serializable
    object Ocean : AlbumTheme(
        name = "Ocean",
        primaryColor = 0xFF4978E1,
        secondaryColor = 0xFFEAEAF0,
        primaryTextColor = 0xFFFFFFFF,
        secondaryTextColor = 0xFF0D0D0D
    )

    @Serializable
    object Flamingo : AlbumTheme(
        name = "Flamingo",
        primaryColor = 0xFFE2A0D7,
        secondaryColor = 0xFF313265,
        primaryTextColor = 0xFF0D0D0D,
        secondaryTextColor = 0xFFFFFFFF
    )

    @Serializable
    object Lavender : AlbumTheme(
        name = "Lavender",
        primaryColor = 0xFFEDF1F4,
        secondaryColor = 0xFF313265,
        primaryTextColor = 0xFF0D0D0D,
        secondaryTextColor = 0xFFFFFFFF
    )

}

val themes = listOf<AlbumTheme>(
    AlbumTheme.Plum,
    AlbumTheme.Ocean,
    AlbumTheme.Flamingo,
    AlbumTheme.Lavender,
    AlbumTheme.Emerald,
)

fun Long.toAlbumTheme(): AlbumTheme? {
    return themes.firstOrNull { it.id == this }
}