package com.picprogress.ds.components.toast

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.picprogress.R
import com.picprogress.model.toast.ToastType
import com.picprogress.model.toast.ToastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppToastState(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
) {
    var message by mutableStateOf<AppToastMessage?>(null)
    fun show(
        toastMessage: ToastMessage,
        type: ToastType,
        duration: Long = 4000,
    ) = coroutineScope.launch {
        this@AppToastState.message = AppToastMessage(
            title = context.getString(toastMessage.getTitle()),
            description = context.getString(toastMessage.getDescription()),
            type = type
        )
        delay(duration)
        this@AppToastState.message = null
    }

    fun dismiss() {
        this.message = null
    }
}

@StringRes
fun ToastMessage.getTitle(): Int = when(this) {
    ToastMessage.DeleteAlbumFailure -> R.string.toast_delete_album_failure_title
    ToastMessage.DeleteAlbumPhotosFailure -> R.string.toast_delete_photos_failure_title
    ToastMessage.DeletePhotoFailure -> R.string.toast_delete_photo_failure_title
    ToastMessage.AddPhotoFailure -> R.string.toast_add_photo_failure_title
}

@StringRes
fun ToastMessage.getDescription(): Int = when(this) {
    ToastMessage.DeleteAlbumFailure -> R.string.toast_delete_album_failure_description
    ToastMessage.DeleteAlbumPhotosFailure -> R.string.toast_delete_photos_failure_description
    ToastMessage.DeletePhotoFailure -> R.string.toast_delete_photo_failure_description
    ToastMessage.AddPhotoFailure -> R.string.toast_add_photo_failure_description
}