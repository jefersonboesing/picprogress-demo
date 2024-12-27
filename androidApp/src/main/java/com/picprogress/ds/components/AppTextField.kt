package com.picprogress.ds.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.ds.theme.AppTheme

@ExperimentalFoundationApi
@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    helpText: String? = null,
    placeholderText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) { Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                if (placeholderText != null) {
                    Text(text = placeholderText, style = MaterialTheme.typography.bodyMedium)
                }
            },
            isError = errorText != null,
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF7F7F9),
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF7F7F9),
                focusedBorderColor = Color.Transparent,
                disabledContainerColor = Color(0xFFF7F7F9),
                disabledBorderColor = Color.Transparent,
                errorContainerColor = Color(0xFFF7F7F9),
                errorBorderColor = Color(0xFFF31527),
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            keyboardOptions = keyboardOptions,
        )
        Spacer(modifier = Modifier.size(4.dp))
        if (errorText != null) ErrorText(text = errorText)
        if (helpText != null) HelpText(text = helpText)
    }
}

@Composable
private fun HelpText(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF646464))
}

@Composable
private fun ErrorText(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color(0xFFF31527))
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun AppTextFieldPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(24.dp)
        ) {
            AppTextField(
                label = "Label",
                value = "",
                onValueChange = {},
                errorText = "Error"
            )
        }
    }
}