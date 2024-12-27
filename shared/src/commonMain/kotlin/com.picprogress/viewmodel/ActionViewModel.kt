package com.picprogress.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ActionViewModel<T> {
    val actionState: StateFlow<ActionState<T>>
    fun runAction(action: T)
    fun onActionStateProcessed(state: ActionState<T>)
}

class ActionViewModelImpl<T> : ActionViewModel<T> {
    private val _actionState = MutableStateFlow<ActionState<T>>(ActionState.Processed())

    override val actionState = _actionState.asStateFlow()

    override fun runAction(action: T) {
        _actionState.update { ActionState.Triggered(action) }
    }

    override fun onActionStateProcessed(state: ActionState<T>) {
        _actionState.compareAndSet(state, ActionState.Processed())
    }
}

sealed class ActionState<T> {
    class Processed<T> : ActionState<T>()
    data class Triggered<T>(val action: T, val isNavigation: Boolean = true) : ActionState<T>()
}
