package com.picprogress.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual abstract class BaseViewModel<S, A> : ViewModel(), StateViewModel<S>, ActionViewModel<A> by ActionViewModelImpl() {
    actual val coroutineScope: CoroutineScope = viewModelScope
}