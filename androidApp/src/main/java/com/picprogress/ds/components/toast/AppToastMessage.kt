package com.picprogress.ds.components.toast

import com.picprogress.model.toast.ToastType

data class AppToastMessage(
    val title: String,
    val description: String,
    val type: ToastType = ToastType.Success,
)