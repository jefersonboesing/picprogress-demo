package com.picprogress.ds.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors

@Composable
fun EmptyScreen(
    @DrawableRes image: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(AppColors.Neutral.High.Lightest),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = image), contentDescription = null)
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = AppColors.Primary.Darkest,
            modifier = Modifier.padding(horizontal = 55.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.Primary.Light,
            modifier = Modifier.padding(top = 20.dp).padding(horizontal = 55.dp),
            textAlign = TextAlign.Center
        )
    }
}
