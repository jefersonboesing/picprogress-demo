@file:OptIn(ExperimentalMaterial3Api::class)

package com.picprogress.ds.components.toast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.picprogress.R
import com.picprogress.ds.theme.AppColors
import com.picprogress.model.toast.ToastType
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppToastHost(
    state: AppToastState,
) {
    state.message?.let { message ->
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { state.dismiss() },
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { state.dismiss() }
                .top()
        ) {
            val windowProvider = LocalView.current.parent as? DialogWindowProvider
            windowProvider?.window?.setDimAmount(0f)
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                color = AppColors.Neutral.High.Light,
                shape = RoundedCornerShape(12.dp),
            ) {
                AppToastContent(message)
            }
        }
    }
}

@Composable
private fun AppToastContent(message: AppToastMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        ) {
            Text(
                text = message.title,
                style = MaterialTheme.typography.titleMedium,
                color = AppColors.Neutral.Low.Darkest,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = message.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Neutral.Low.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Image(
            painter = painterResource(id = message.type.toIcon()),
            contentDescription = null
        )
    }
}

private fun Modifier.top() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
        placeable.place(0, 0)
    }
}

@DrawableRes
private fun ToastType.toIcon(): Int = when (this) {
    ToastType.Success -> R.drawable.ic_success
    ToastType.Error -> R.drawable.ic_error
}

@Preview
@Composable
private fun AppToastPreview() {
    AppToastContent(
        message = AppToastMessage(
            title = "Message",
            description = "Notification Description",
            type = ToastType.Error
        )
    )
}

@Composable
fun rememberAppToastState(coroutineScope: CoroutineScope): AppToastState {
    val context = LocalContext.current
    return remember { AppToastState(context, coroutineScope) }
}
