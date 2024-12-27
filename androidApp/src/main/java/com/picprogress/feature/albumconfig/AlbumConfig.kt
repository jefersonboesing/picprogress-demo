package com.picprogress.feature.albumconfig

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.picprogress.model.album.Album
import com.picprogress.ds.util.navigation.ArgAppRoute
import com.picprogress.ds.util.navigation.bottomSheet
import com.picprogress.ds.util.navigation.build
import com.picprogress.ds.util.navigation.getArgs
import kotlinx.serialization.Serializable

@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.albumConfigSheet(
    onCloseClick: () -> Unit,
) {
    bottomSheet(AlbumConfigRoute) {
        AlbumConfigSheet(
            albumConfigArgs = AlbumConfigRoute.getArgs(it.arguments) ?: throw IllegalStateException(),
            onCloseClick = onCloseClick
        )
    }
}

fun NavController.navigateToAlbumConfig(album: Album? = null) {
    navigate(AlbumConfigRoute.build(AlbumConfigArgs(album)))
}

object AlbumConfigRoute : ArgAppRoute<AlbumConfigArgs>("album-config")

@Serializable
data class AlbumConfigArgs(val album: Album?)