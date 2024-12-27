package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.picprogress.ds.theme.AppColors

@Composable
fun AppPrompt(
    @DrawableRes icon: Int,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(AppColors.Neutral.High.Lightest, shape = RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .background(AppColors.Primary.Lightest, RoundedCornerShape(100.dp))
                    .padding(24.dp)
                    .size(40.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = AppColors.Neutral.Low.Darkest,
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = AppColors.Neutral.Low.Medium,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    text = negativeButtonText,
                    type = AppButtonType.Secondary,
                    modifier = Modifier.weight(1f),
                    onClick = onNegativeButtonClick
                )
                AppButton(
                    text = positiveButtonText,
                    type = AppButtonType.Primary,
                    modifier = Modifier.weight(1f),
                    onClick = onPositiveButtonClick
                )
            }
        }
    }
}
