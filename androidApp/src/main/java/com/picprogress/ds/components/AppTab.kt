package com.picprogress.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.theme.AppColors


@Composable
fun AppTab(
    tabs: List<AppIconTab>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onTabClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .wrapContentWidth()
            .background(AppColors.Primary.Lightest, shape = RoundedCornerShape(48.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        val positions = remember { mutableStateListOf<Offset>() }
        val indicatorXOffset by animateIntAsState(
            targetValue = positions.getOrNull(selectedIndex)?.x?.toInt() ?: 0,
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
            label = "indicatorXOffset"
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(x = indicatorXOffset, y = 0) }
                .padding(4.dp)
                .background(AppColors.Primary.Medium, shape = RoundedCornerShape(48.dp))
                .width(64.dp)
                .height(40.dp)
        )

        Row {
            tabs.forEachIndexed { index, tab ->
                Icon(
                    modifier = Modifier
                        .onGloballyPositioned {
                            if (positions.size <= index) {
                                positions.add(it.positionInParent())
                            } else {
                                positions[index] = it.positionInParent()
                            }
                        }
                        .clip(RoundedCornerShape(48.dp))
                        .clickable { onTabClick(index) }
                        .padding(horizontal = 24.dp)
                        .padding(vertical = 12.dp),
                    painter = painterResource(id = tab.icon),
                    contentDescription = null,
                    tint = if (index == selectedIndex) AppColors.Primary.Lightest else AppColors.Primary.Medium
                )
            }

        }
    }
}

data class AppIconTab(
    @DrawableRes val icon: Int
)

@Preview
@Composable
fun AppTabPreview() {
    var currentIndexSelected by remember { mutableIntStateOf(0) }

    AppTab(
        tabs = listOf(
            AppIconTab(icon = R.drawable.ic_side_by_side),
            AppIconTab(icon = R.drawable.ic_magic_wand)
        ),
        selectedIndex = currentIndexSelected,
        onTabClick = { currentIndexSelected = it }
    )
}