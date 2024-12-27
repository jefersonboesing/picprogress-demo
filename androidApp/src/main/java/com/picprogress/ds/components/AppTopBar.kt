package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors
import com.picprogress.util.truncate

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleAlign: TextAlign = TextAlign.Center,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    startAction: AppTopBarAction? = null,
    endActions: List<AppTopBarAction> = listOf(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    mode: AppTopBarMode = AppTopBarMode.LIGHT
) {
    val titleHorizontalPadding = if (startAction != null) 52.dp else 0.dp
    CompositionLocalProvider(LocalContentColor provides mode.getForegroundColor()) {
        Box(
            modifier = modifier
                .background(mode.getBackgroundColor())
                .fillMaxWidth()
                .height(48.dp)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            AppTopBarActions(
                startAction = startAction,
                endActions = endActions
            )
            Text(
                text = title.truncate(20),
                style = titleStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = titleHorizontalPadding),
                textAlign = titleAlign
            )
        }
    }
}

@Composable
private fun AppTopBarActions(
    startAction: AppTopBarAction? = null,
    endActions: List<AppTopBarAction> = listOf(),
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (startAction != null) AppTopBarActionItem(startAction)

        Spacer(modifier = Modifier.weight(1f))
        endActions.forEach {
            AppTopBarActionItem(it)
        }
    }
}

@Composable
private fun AppTopBarActionItem(action: AppTopBarAction) {
    when (action) {
        is AppTopBarAction.Button -> AppButtonSmall(text = action.text, onClick = action.onClick)
        is AppTopBarAction.Icon -> AppTopBarIcon(icon = action.icon, onClick = action.onClick)
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    AppTopBar(
        title = "New album",
        startAction = AppTopBarAction.Icon(
            icon = R.drawable.ic_arrow_down,
            onClick = {}
        ),
        endActions = listOf(
            AppTopBarAction.Icon(
                icon = R.drawable.ic_plus,
                onClick = {}
            )
        )
    )
}

enum class AppTopBarMode {
    LIGHT, DARK
}

fun AppTopBarMode.getBackgroundColor() = when (this) {
    AppTopBarMode.LIGHT -> AppColors.Neutral.High.Lightest
    AppTopBarMode.DARK -> AppColors.Neutral.Low.Darkest
}

fun AppTopBarMode.getForegroundColor() = when(this) {
    AppTopBarMode.LIGHT -> AppColors.Neutral.Low.Darkest
    AppTopBarMode.DARK -> AppColors.Neutral.High.Lightest
}

sealed class AppTopBarAction {
    data class Icon(@DrawableRes val icon: Int, val onClick: () -> Unit): AppTopBarAction()
    data class Button(val text: String, val onClick: () -> Unit): AppTopBarAction()
}