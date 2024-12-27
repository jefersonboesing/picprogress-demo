@file:OptIn(ExperimentalMaterial3Api::class)

package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun OptionsSheet(
    title: String?,
    options: List<Option>,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 56.dp,
                height = 6.dp,
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFCCCCCC)
            )
        },
        contentWindowInsets = { WindowInsets(0.dp) },
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(20.dp))
            }
            options.forEach {
                AppListItem(
                    label = it.text,
                    endIcon = it.icon,
                    selected = it.selected
                ) {
                    coroutineScope.launch {
                        sheetState.hide()
                        it.onClick()
                    }
                }
            }
            Spacer(modifier = Modifier.size(54.dp))
        }
    }
}

data class Option(
    val text: String,
    @DrawableRes val icon: Int? = null,
    val selected: Boolean = false,
    val onClick: () -> Unit,
)