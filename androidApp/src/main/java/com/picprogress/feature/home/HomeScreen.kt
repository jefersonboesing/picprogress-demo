package com.picprogress.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.picprogress.R
import com.picprogress.ds.components.AppFilterButton
import com.picprogress.ds.components.AppFloatingActionButton
import com.picprogress.ds.components.card.AlbumCard
import com.picprogress.ds.screen.EmptyScreen
import com.picprogress.ds.components.Option
import com.picprogress.ds.components.OptionsSheet
import com.picprogress.ds.theme.AppColors
import com.picprogress.ds.components.AppTopBar
import com.picprogress.ds.components.AppTopBarAction
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumWithSummary
import com.picprogress.viewmodel.HomeViewModel.AlbumFilterType
import com.picprogress.viewmodel.HomeViewModel.AlbumSortType
import com.picprogress.viewmodel.HomeViewModel.HomeContentMode
import com.picprogress.viewmodel.HomeViewModel.State

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    state: State,
    onAlbumClick: (Album) -> Unit,
    onNewAlbumClick: () -> Unit,
    onContentModeChangeClick: () -> Unit,
    onOpenFilterTypeClick: () -> Unit,
    onOpenSortTypeClick: () -> Unit,
    onFilterTypeChange: (AlbumFilterType) -> Unit,
    onSortTypeChange: (AlbumSortType) -> Unit,
    onDialogDismiss: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .background(AppColors.Neutral.High.Lightest)
            .statusBarsPadding(),
        containerColor = AppColors.Neutral.High.Lightest,
        topBar = {
            Column(modifier = Modifier.background(AppColors.Neutral.High.Lightest)) {
                AppTopBar(
                    title = stringResource(id = R.string.home_screen_title),
                    titleAlign = TextAlign.Start,
                    titleStyle = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                    endActions = listOf(
                        AppTopBarAction.Icon(
                            icon = if (state.contentMode == HomeContentMode.LIST) R.drawable.ic_grid else R.drawable.ic_list,
                            onClick = onContentModeChangeClick
                        )
                    )
                )
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppFilterButton(
                        text = state.filterType.toText(),
                        icon = R.drawable.ic_arrow_up_down,
                        onClick = onOpenFilterTypeClick
                    )
                    AppFilterButton(
                        text = state.sortType.toText(),
                        icon = R.drawable.ic_arrow_down_2,
                        onClick = onOpenSortTypeClick
                    )
                }
            }
        },
        floatingActionButton = {
            AppFloatingActionButton(icon = R.drawable.ic_plus, onClick = onNewAlbumClick)
        }
    ) { paddingValues ->

        if (state.isEmptyViewVisible) {
            EmptyScreen(
                image = R.drawable.ic_no_albums,
                title = state.filterType.getEmptyViewTitle(),
                description = state.filterType.getEmptyViewDescription(),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AnimatedContent(
                targetState = state.contentMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220, delayMillis = 90)).togetherWith(
                        fadeOut(animationSpec = tween(90))
                    )
                },
                label = ""
            ) { mode ->
                when (mode) {
                    HomeContentMode.LIST -> AlbumList(
                        modifier = Modifier.padding(paddingValues),
                        albumsWithSummary = state.albumsWithSummary,
                        onAlbumClick = onAlbumClick
                    )

                    HomeContentMode.GRID -> AlbumGrid(
                        modifier = Modifier.padding(paddingValues),
                        albumsWithSummary = state.albumsWithSummary,
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        }

        if (state.isFilterTypeDialogVisible) {
            FilterTypeOptions(
                selectedFilterType = state.filterType,
                onFilterTypeChange = onFilterTypeChange,
                onDismiss = onDialogDismiss
            )
        }

        if (state.isSortTypeDialogVisible) {
            SortTypeOptions(
                selectedSortType = state.sortType,
                onSortTypeChange = onSortTypeChange,
                onDismiss = onDialogDismiss
            )
        }
    }
}

@Composable
fun AlbumList(
    modifier: Modifier,
    albumsWithSummary: List<AlbumWithSummary>,
    onAlbumClick: (Album) -> Unit,
) {
    AnimatedVisibility(
        visible = albumsWithSummary.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyColumn(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(albumsWithSummary) {
                AlbumCard(
                    album = it.album,
                    summary = it.summary
                ) {
                    onAlbumClick(it.album)
                }
            }
        }
    }
}


@Composable
fun AlbumGrid(
    modifier: Modifier,
    albumsWithSummary: List<AlbumWithSummary>,
    onAlbumClick: (Album) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(albumsWithSummary) {
            AlbumCard(
                album = it.album,
                summary = it.summary,
                isSmallMode = true
            ) {
                onAlbumClick(it.album)
            }
        }
    }
}

@Composable
fun FilterTypeOptions(
    selectedFilterType: AlbumFilterType,
    onFilterTypeChange: (AlbumFilterType) -> Unit,
    onDismiss: () -> Unit
) {
    OptionsSheet(
        title = null,
        options = AlbumFilterType.values().map {
            Option(
                text = it.toText(),
                selected = selectedFilterType == it,
                onClick = { onFilterTypeChange(it) }
            )
        },
        onDismiss = onDismiss
    )
}

@Composable
fun SortTypeOptions(
    selectedSortType: AlbumSortType,
    onSortTypeChange: (AlbumSortType) -> Unit,
    onDismiss: () -> Unit
) {
    OptionsSheet(
        title = null,
        options = AlbumSortType.values().map {
            Option(
                text = it.toText(),
                selected = selectedSortType == it,
                onClick = { onSortTypeChange(it) }
            )
        },
        onDismiss = onDismiss
    )
}

@Composable
private fun AlbumFilterType.getEmptyViewTitle(): String = when(this) {
    AlbumFilterType.ALL -> stringResource(id = R.string.home_screen_empty_title)
    AlbumFilterType.DAILY -> stringResource(id = R.string.home_screen_empty_title_daily)
    AlbumFilterType.WEEKLY -> stringResource(id = R.string.home_screen_empty_title_weekly)
    AlbumFilterType.MONTHLY -> stringResource(id = R.string.home_screen_empty_title_monthly)
}

@Composable
private fun AlbumFilterType.getEmptyViewDescription(): String = when(this) {
    AlbumFilterType.ALL -> stringResource(id = R.string.home_screen_empty_description)
    AlbumFilterType.DAILY -> stringResource(id = R.string.home_screen_empty_description_daily)
    AlbumFilterType.WEEKLY -> stringResource(id = R.string.home_screen_empty_description_weekly)
    AlbumFilterType.MONTHLY -> stringResource(id = R.string.home_screen_empty_description_monthly)
}

@Composable
private fun AlbumFilterType.toText(): String = when (this) {
    AlbumFilterType.ALL -> stringResource(id = R.string.home_screen_filter_all)
    AlbumFilterType.DAILY -> stringResource(id = R.string.home_screen_filter_daily)
    AlbumFilterType.WEEKLY -> stringResource(id = R.string.home_screen_filter_weekly)
    AlbumFilterType.MONTHLY -> stringResource(id = R.string.home_screen_filter_monthly)
}

@Composable
private fun AlbumSortType.toText(): String = when (this) {
    AlbumSortType.DATE -> stringResource(id = R.string.home_screen_sort_date)
    AlbumSortType.TITLE -> stringResource(id = R.string.home_screen_sort_title)
}