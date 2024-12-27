package com.picprogress.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.native.concurrent.freeze

actual abstract class BaseViewModel<S, A> : StateViewModel<S>, ActionViewModel<A> by ActionViewModelImpl() {
    actual val coroutineScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + Job()
    )

    fun watchState(callback: (S) -> Unit): Job {
        return uiState.watch(callback)
    }

    fun watchAction(callback: (ActionState<A>) -> Unit): Job {
        return actionState.watch(callback)
    }

    private fun <T> StateFlow<T>.watch(callback: (T) -> Unit): Job {
        return coroutineScope.launch {
            collect { value ->
                callback(value.freeze())  // Freeze the value before passing it to the callback
            }
        }
    }

}