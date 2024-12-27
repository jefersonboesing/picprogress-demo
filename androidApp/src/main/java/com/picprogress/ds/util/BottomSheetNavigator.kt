package com.picprogress.ds.util

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterialNavigationApi
@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SpringSpec<Float>(),
    confirmValueChange: (ModalBottomSheetValue) -> Boolean = { false },
    skipHalfExpanded: Boolean = false,
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec,
        confirmValueChange,
        skipHalfExpanded,
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}