package com.picprogress.ds.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.picprogress.viewmodel.ActionState

@Composable
fun <T> ActionEffect(
    actionState: ActionState<T>,
    onActionStateProcessed: (ActionState<T>) -> Unit,
    onAction: (T) -> Unit
) {
    LaunchedEffect(key1 = actionState) {
        when (actionState) {
            is ActionState.Processed -> {}
            is ActionState.Triggered -> {
                if (actionState.isNavigation) {
                    onAction(actionState.action)
                    onActionStateProcessed(actionState)
                } else {
                    onAction(actionState.action)
                    onActionStateProcessed(actionState)
                }
            }
        }
    }
}