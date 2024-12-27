package com.picprogress.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors

@Composable
fun <T> AppDropdown(
    items: Map<String, T>,
    selectedItem: Pair<String, T>,
    modifier: Modifier = Modifier,
    onItemSelected: (Pair<String, T>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(6.dp)
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(AppColors.Neutral.High.Lightest, shape)
                .widthIn(min = 140.dp)
                .clip(shape = shape)
                .clickable { expanded = true }
                .padding(vertical = 8.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedItem.first,
                modifier = Modifier
                    .widthIn(min = 90.dp)
                    .weight(1f),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = AppColors.Primary.Medium
            )
        }

        DropdownMenu(
            modifier = Modifier
                .background(AppColors.Neutral.High.Lightest, shape)
                .heightIn(max = 340.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownItem(item) {
                    onItemSelected(it)
                    expanded = false
                }
            }
        }
    }
}

@Composable
private fun <T> DropdownItem(item: Map.Entry<String, T>, onItemSelected: (Pair<String, T>) -> Unit) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = item.key,
            modifier = Modifier
                .background(AppColors.Neutral.High.Lightest, shape)
                .fillMaxWidth()
                .widthIn(min = 90.dp)
                .clip(shape = shape)
                .clickable {
                    onItemSelected(item.toPair())
                }
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )
    }
}