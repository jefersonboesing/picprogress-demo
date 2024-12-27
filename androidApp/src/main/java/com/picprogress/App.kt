@file:OptIn(ExperimentalMaterialApi::class)

package com.picprogress

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.theme.AppTheme
import com.picprogress.ds.util.rememberBottomSheetNavigator
import com.picprogress.feature.album.albumScreen
import com.picprogress.feature.album.navigateToAlbum
import com.picprogress.feature.albumconfig.albumConfigSheet
import com.picprogress.feature.albumconfig.navigateToAlbumConfig
import com.picprogress.feature.camera.CameraRoute
import com.picprogress.feature.camera.cameraScreen
import com.picprogress.feature.camera.navigateToCamera
import com.picprogress.feature.compare.compareScreen
import com.picprogress.feature.compare.navigateToCompare
import com.picprogress.feature.home.HomeRoute
import com.picprogress.feature.home.homeScreen
import com.picprogress.feature.photo.navigateToPhoto
import com.picprogress.feature.photo.photoScreen
import com.picprogress.feature.photoconfig.navigateToPhotoConfig
import com.picprogress.feature.photoconfig.photoConfigSheet
import com.picprogress.feature.photoselection.navigateToPhotoSelection
import com.picprogress.feature.photoselection.photoSelectionSheet
import com.picprogress.feature.preview.navigateToPreview
import com.picprogress.feature.preview.previewScreen
import com.picprogress.ds.util.navigation.ScreenResultKeys
import com.picprogress.ds.util.navigation.build
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition

@ExperimentalMaterialNavigationApi
@ExperimentalMaterial3Api
@Composable
fun App() {
    AppTheme {
        val bottomSheetNavigator = rememberBottomSheetNavigator(
            skipHalfExpanded = true,
            confirmValueChange = { it != ModalBottomSheetValue.Hidden }
        )
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetElevation = 8.dp
        ) {
            val navController = rememberNavController(bottomSheetNavigator)
            CompositionLocalProvider(LocalNavigationProvider provides navController) {
                NavHost(
                    modifier = Modifier.background(AppColors.Neutral.High.Lightest),
                    navController = navController,
                    startDestination = HomeRoute.build()
                ) {
                    homeScreen(
                        onNewAlbumClick = { navController.navigateToAlbumConfig() },
                        onAlbumClick = {
                            navController.navigateToAlbum(it)
                        },
                        bottomSheetNavigatorSheetState = bottomSheetNavigator.navigatorSheetState
                    )
                    albumConfigSheet(
                        onCloseClick = {
                            navController.popBackStack()
                        }
                    )
                    albumScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onOpenCameraClick = {
                            navController.navigateToCamera(it)
                        },
                        onOpenPhotoClick = { album, photo ->
                            navController.navigateToPhoto(album, photo)
                        },
                        onGoToCompare = { album ->
                            navController.navigateToCompare(album)
                        },
                        onGoToEdit = { album ->
                            navController.navigateToAlbumConfig(album)
                        },
                        bottomSheetNavigatorSheetState = bottomSheetNavigator.navigatorSheetState
                    )
                    cameraScreen(
                        onCloseClick = {
                            navController.popBackStack()
                        },
                        onGoToPreview = { albumId, photoPath ->
                            navController.navigateToPreview(albumId, photoPath)
                        }
                    )
                    previewScreen(
                        onCloseClick = {
                            navController.popBackStack(CameraRoute.template, inclusive = true)
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                    photoScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onGoToCompare = { album, comparePhotos ->
                            navController.navigateToCompare(album, comparePhotos)
                        },
                        onGoToEdit = {
                            navController.navigateToPhotoConfig(it)
                        },
                        onOpenSelectToCompare = { album, unavailablePhotos ->
                            navController.navigateToPhotoSelection(
                                album = album,
                                minRequired = 1,
                                unavailablePhotos = unavailablePhotos
                            )
                        },
                        bottomSheetNavigatorSheetState = bottomSheetNavigator.navigatorSheetState
                    )
                    photoConfigSheet(
                        onCloseClick = {
                            navController.popBackStack()
                        }
                    )
                    compareScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onOpenSelectToCompare = { album, initialSelection ->
                            navController.navigateToPhotoSelection(
                                album = album,
                                minRequired = 2,
                                initialSelection = initialSelection
                            )
                        }
                    )
                    photoSelectionSheet(
                        onCloseClick = {
                            navController.popBackStack()
                        },
                        onPhotosSelected = { photos ->
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                key = ScreenResultKeys.SelectedPhotosKey,
                                value = photos
                            )
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.koinSharedViewModel(noinline parameters: ParametersDefinition? = null): T {
    val navController = LocalNavigationProvider.current
    val navGraphRoute = destination.parent?.route ?: return koinViewModel(parameters = parameters)
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry, parameters = parameters)
}

val LocalNavigationProvider = compositionLocalOf<NavController> { error("No NavController provided") }