package com.picprogress.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

expect abstract class BaseViewModel<S, A>() : StateViewModel<S>, ActionViewModel<A> {
    val coroutineScope: CoroutineScope

    override val actionState: StateFlow<ActionState<A>>
    override fun runAction(action: A)
    override fun onActionStateProcessed(state: ActionState<A>)
}