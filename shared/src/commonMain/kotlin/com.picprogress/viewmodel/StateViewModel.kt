package com.picprogress.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<T> {
    val uiState: StateFlow<T>
}