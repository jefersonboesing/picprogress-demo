package com.picprogress.feature.photoconfig

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppButton
import com.picprogress.ds.components.AppDatePicker
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.util.format
import com.picprogress.viewmodel.PhotoConfigViewModel.State
import kotlinx.datetime.LocalDate

@Composable
fun PhotoConfigSheet(
    state: State,
    onCloseClick: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = AppColors.Neutral.High.Lightest)
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTopBar(
            title = stringResource(id = R.string.photo_config_sheet_title),
            startAction = AppTopBarAction.Icon(
                icon = R.drawable.ic_arrow_down,
                onClick = onCloseClick
            ),
        )
        DateDescription(description = stringResource(id = R.string.photo_config_sheet_original), date = state.originalDate)
        Spacer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(color = AppColors.Neutral.High.Medium)
                .fillMaxWidth()
                .height(1.dp)
        )
        DateDescription(description = stringResource(id = R.string.photo_config_sheet_adjusted), date = state.adjustedDate, dateColor = AppColors.Neutral.Low.Darkest)
        AppDatePicker(selected = state.adjustedDate, onDateChange = onDateChange, modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.weight(1f))
        AppButton(text = stringResource(id = R.string.photo_config_sheet_save_button), onClick = onSaveClick, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp))

    }
}

@Composable
private fun DateDescription(
    description: String,
    date: LocalDate,
    dateColor: Color = AppColors.Neutral.Low.Light,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(
            text = description,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
            color = AppColors.Neutral.Low.Light
        )
        Text(
            text = date.format(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            color = dateColor
        )
    }
}