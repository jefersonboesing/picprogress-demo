package com.picprogress.ds.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.picprogress.ds.theme.AppColors

@Composable
fun TutorialScreen(
    @DrawableRes icon: Int,
    message: String
) {
    Column(
        modifier = Modifier
            .background(AppColors.Additional.Tutorial)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.4f))
        Image(painter = painterResource(id = icon), contentDescription = null)
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.Neutral.High.Lightest,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}