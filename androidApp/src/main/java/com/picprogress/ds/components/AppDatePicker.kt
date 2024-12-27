@file:OptIn(ExperimentalLayoutApi::class)

package com.picprogress.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.ds.theme.AppColors
import com.picprogress.util.isToday
import com.picprogress.util.name
import com.picprogress.util.today
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.time.format.TextStyle

@Composable
fun AppDatePicker(
    selected: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedMonth by remember { mutableStateOf(selected.month) }
    var selectedYear by remember { mutableStateOf(selected.year) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.Neutral.High.Lightest, RoundedCornerShape(12.dp))
            .border(1.dp, AppColors.Neutral.High.Medium, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DatePickerHeader(
            month = selectedMonth,
            year = selectedYear,
            onMonthChange = { selectedMonth = it },
            onYearChange = { selectedYear = it }
        )
        CalendarLabels()
        Calendar(
            selected = selected,
            month = selectedMonth,
            year = selectedYear,
            onDateChanged = onDateChange
        )
    }
}

@Composable
private fun DatePickerHeader(
    month: Month,
    year: Int,
    onMonthChange : (Month) -> Unit,
    onYearChange: (Int) -> Unit
) {
    val months = Month.values().associateBy { it.name() }
    val years = ((today().year - 20)..today().year).sortedDescending().associateBy { it.toString() }

    Row(modifier = Modifier) {
        AppDropdown(
            modifier = Modifier.weight(1f),
            items = months,
            selectedItem = month.name() to month,
            onItemSelected = { onMonthChange(it.second) }
        )
        Spacer(modifier = Modifier.size(12.dp))
        AppDropdown(
            modifier = Modifier.weight(1f),
            items = years,
            selectedItem = year.toString() to year,
            onItemSelected = { onYearChange(it.second) })
    }
}

@Composable
private fun CalendarLabels() {
    Row(modifier = Modifier.fillMaxWidth()) {
        DayOfWeek.values().forEach {
            Text(
                text = it.name(TextStyle.SHORT).uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.Primary.Light,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun Calendar(selected: LocalDate, month: Month, year: Int, onDateChanged: (LocalDate) -> Unit) {
    val calendarItems = createCalendarItems(month, year)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        maxItemsInEachRow = 7
    ) {
        (calendarItems).forEach { type ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                when (type) {
                    is CalendarItem.Day -> {
                        val date = LocalDate(year = year, month = month, dayOfMonth = type.day)
                        CalendarDay(date = date, selected = date == selected, enabled = date <= today()) {
                            onDateChanged(date)
                        }
                    }
                    CalendarItem.None -> {}
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(size = 24.dp)
    
    val modifier: Modifier = when {
        selected -> Modifier.background(color = AppColors.Primary.Medium, shape = shape)
        date.isToday() -> Modifier.border(width = 2.dp, color = AppColors.Primary.Light, shape = shape)
        else -> Modifier
    }

    val textColor = when {
        selected -> AppColors.Neutral.High.Lightest
        date <= today() -> AppColors.Primary.Darkest
        else -> AppColors.Neutral.Low.Lightest
    }

    Text(
        text = date.dayOfMonth.toString(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        modifier = modifier
            .width(32.dp)
            .height(40.dp)
            .clip(shape)
            .clickable(enabled = enabled) {
                onClick()
            }
            .padding(vertical = 12.dp)
    )
}

private fun createCalendarItems(month: Month, year: Int): List<CalendarItem> {
    val monthStart = LocalDate(year = year, month = month, dayOfMonth = 1)
    val monthEnd = monthStart.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
    val calendarItems = mutableListOf<CalendarItem>()
    repeat(monthStart.dayOfWeek.value - DayOfWeek.MONDAY.value) {
        calendarItems.add(CalendarItem.None)
    }
    (monthStart.dayOfMonth..monthEnd.dayOfMonth).forEach {
        calendarItems.add(CalendarItem.Day(it))
    }
    repeat(DayOfWeek.SUNDAY.value - monthEnd.dayOfWeek.value) {
        calendarItems.add(CalendarItem.None)
    }
    return calendarItems
}

private sealed class CalendarItem {
    data object None : CalendarItem()
    data class Day(val day: Int) : CalendarItem()
}
