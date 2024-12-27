package com.picprogress.viewmodel

import com.picprogress.viewmodel.HomeViewModel.Action
import com.picprogress.viewmodel.HomeViewModel.State
import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumWithSummary
import com.picprogress.model.album.Frequency
import com.picprogress.usecase.GetAlbumsWithSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAlbumsWithSummaryUseCase: GetAlbumsWithSummaryUseCase
) : BaseViewModel<State, Action>() {

    private var allAlbumsWithSummary = listOf<AlbumWithSummary>()
    private val _uiState = MutableStateFlow(State())
    override val uiState: StateFlow<State> = _uiState.asStateFlow()


    init {
        onRefresh()
    }

    fun onRefresh() = coroutineScope.launch {
        val result = getAlbumsWithSummaryUseCase.invoke(Unit)
        if (result is Result.Success) {
            resetFiltersIfNeeded(result.data)
            allAlbumsWithSummary = result.data
            refreshAlbumsAndApplyFilters()
        }
    }

    fun onNewAlbumClick() {
        runAction(Action.GoToNewAlbum)
    }

    fun onAlbumClick(album: Album) {
        runAction(Action.GoToAlbum(album))
    }

    fun onContentModeChangeClick() = _uiState.update {
        it.copy(
            contentMode = when (it.contentMode) {
                HomeContentMode.LIST -> HomeContentMode.GRID
                HomeContentMode.GRID -> HomeContentMode.LIST
            }
        )
    }

    fun onOpenFilterTypeClick() = _uiState.update {
        it.copy(isFilterTypeDialogVisible = true)
    }

    fun onOpenSortTypeClick() = _uiState.update {
        it.copy(isSortTypeDialogVisible = true)
    }

    fun onDialogDismiss() = _uiState.update {
        it.copy(
            isFilterTypeDialogVisible = false,
            isSortTypeDialogVisible = false
        )
    }

    fun onSortTypeChange(sortType: AlbumSortType) = _uiState.update {
        it.copy(sortType = sortType, isSortTypeDialogVisible = false)
    }.also { refreshAlbumsAndApplyFilters() }

    fun onFilterTypeChange(filterType: AlbumFilterType) = _uiState.update {
        it.copy(filterType = filterType, isFilterTypeDialogVisible = false)
    }.also { refreshAlbumsAndApplyFilters() }

    private fun refreshAlbumsAndApplyFilters() = _uiState.update { state ->
        val albumsFiltered = allAlbumsWithSummary.filter {
            when (state.filterType) {
                AlbumFilterType.ALL -> true
                AlbumFilterType.DAILY -> it.album.frequency == Frequency.DAILY
                AlbumFilterType.WEEKLY -> it.album.frequency == Frequency.WEEKLY
                AlbumFilterType.MONTHLY -> it.album.frequency == Frequency.MONTHLY
            }
        }
        val albumsSorted = when (state.sortType) {
            AlbumSortType.DATE -> albumsFiltered.sortedByDescending { it.album.createdAt }
            AlbumSortType.TITLE -> albumsFiltered.sortedBy { it.album.title.lowercase() }
        }
        state.copy(albumsWithSummary = albumsSorted, isEmptyViewVisible = albumsSorted.isEmpty())
    }

    private fun resetFiltersIfNeeded(newItems: List<AlbumWithSummary>) {
        val allAlbumsWithSummaryIds = allAlbumsWithSummary.map { it.album.id }.toSet()
        val resultDataIds = newItems.map { it.album.id }.toSet()
        if (resultDataIds != allAlbumsWithSummaryIds) {
            _uiState.update { it.copy(filterType = AlbumFilterType.ALL) }
        }
    }

    sealed class Action {
        data class GoToAlbum(val album: Album) : Action()
        data object GoToNewAlbum : Action()
    }

    data class State(
        val albumsWithSummary: List<AlbumWithSummary> = emptyList(),
        val isEmptyViewVisible: Boolean = false,
        val isFilterTypeDialogVisible: Boolean = false,
        val isSortTypeDialogVisible: Boolean = false,
        val contentMode: HomeContentMode = HomeContentMode.LIST,
        val sortType: AlbumSortType = AlbumSortType.DATE,
        val filterType: AlbumFilterType = AlbumFilterType.ALL,
    )

    enum class AlbumFilterType {
        ALL, DAILY, WEEKLY, MONTHLY
    }

    enum class AlbumSortType {
        DATE, TITLE
    }

    enum class HomeContentMode {
        LIST, GRID
    }

}