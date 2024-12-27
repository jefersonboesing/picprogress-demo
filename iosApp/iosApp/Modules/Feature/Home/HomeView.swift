import SwiftUI
import shared

struct HomeView: View {
    
    @EnvironmentObject 
    private var appNavigation: AppNavigation
    
    @StateObject
    private var viewModel = Injection().homeViewModel
    
    @StateObject
    private var stateWrapper = StateWrapper(Defaults().stateHome())
    
    let navigateTo: (AppNavigation.Destination) -> Void
    let goBack: () -> Void
    
    var body: some View {
        HomeScreen(
            state: stateWrapper.value,
            onAlbumClick: viewModel.onAlbumClick(album:),
            onNewAlbumClick: viewModel.onNewAlbumClick,
            onContentModeChangeClick: viewModel.onContentModeChangeClick,
            onOpenFilterTypeClick: viewModel.onOpenFilterTypeClick,
            onOpenSortTypeClick: viewModel.onOpenSortTypeClick,
            onDialogDismiss: viewModel.onDialogDismiss,
            onFilterTypeChange: viewModel.onFilterTypeChange(filterType:),
            onSortTypeChange: viewModel.onSortTypeChange(sortType:)
        )
        .onAppear {
            viewModel.onRefresh()
        }
        .onChange(of: appNavigation.activeSheet) { newValue in
            if newValue == nil { viewModel.onRefresh() }
        }
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case let action as HomeViewModel.ActionGoToAlbum:
                return navigateTo(.page(.album(album: action.album)))
            case _ as HomeViewModel.ActionGoToNewAlbum:
                return navigateTo(.sheet(.albumConfig()))
            default:
                return
            }
        }
    }

}

private struct HomeScreen: View {
        
    let state: HomeViewModel.State
    let onAlbumClick: (Album) -> Void
    let onNewAlbumClick: () -> Void
    let onContentModeChangeClick: () -> Void
    let onOpenFilterTypeClick: () -> Void
    let onOpenSortTypeClick: () -> Void
    let onDialogDismiss: () -> Void
    let onFilterTypeChange: (HomeViewModel.AlbumFilterType) -> Void
    let onSortTypeChange: (HomeViewModel.AlbumSortType) -> Void
    
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            ScrollView {
                VStack {
                    switch state.contentMode {
                    case .grid:
                        gridMode
                    default:
                        listMode
                    }
                    if state.isEmptyViewVisible {
                        AppEmptyView(
                            image: .icNoAlbums,
                            title: state.filterType.getEmptyViewTitle(),
                            description: state.filterType.getEmptyViewDescription()
                        )
                    }
                }
            }
            .padding(.top, 16)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .safeAreaInset(edge: .bottom) {
                Color.clear.frame(height: 30)
            }
            AppFloatingActionButton(.icPlus) {
                onNewAlbumClick()
            }
            .padding(.vertical, 20)
            .padding(.horizontal, 16)
        }
        .appToolbar(
            title: "Albums",
            titleFont: .headlineLarge(),
            trailingActions: [
                AppToolbarAction(
                    icon: state.contentMode.toIcon(),
                    action: onContentModeChangeClick
                )
            ]
        )
        .appOptionsSheet(
            isPresented: state.isFilterTypeDialogVisible,
            options: HomeViewModel.AlbumFilterType.entries.map{ filterType in
                AppOption(
                    text: filterType.toText(),
                    selected: state.filterType.isEqual(filterType),
                    onClick: { onFilterTypeChange(filterType) }
                )
            },
            onDismiss: onDialogDismiss
        )
        .appOptionsSheet(
            isPresented: state.isSortTypeDialogVisible,
            options: HomeViewModel.AlbumSortType.entries.map{ sortType in
                AppOption(
                    text: sortType.toText(),
                    selected: state.sortType.isEqual(sortType),
                    onClick: { onSortTypeChange(sortType) }
                )
            },
            onDismiss: onDialogDismiss
        )
        .animation(.spring, value: state.contentMode)
    }
    
    
    private var listMode: some View {
        LazyVStack(spacing: 12, pinnedViews: [.sectionHeaders]) {
            Section(
                header: HStack(spacing: 12) {
                    AppFilterButton(state.filterType.toText(), icon: .icArrowUpDown, action: onOpenFilterTypeClick)
                    AppFilterButton(state.sortType.toText(), icon: .icArrowDown, action: onOpenSortTypeClick)
                }.frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.bottom, 8)
                    .background(.white)
            ) {
                ForEach(state.albumsWithSummary) { albumWitSummary in
                    AlbumCard(
                        album: albumWitSummary.album,
                        isSmallMode: false,
                        summary: albumWitSummary.summary,
                        onClick: {
                            onAlbumClick(albumWitSummary.album)
                        }
                    )
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 60)
    }
    
    private var gridMode: some View {
        LazyVGrid(columns: Array(repeating: .init(spacing: 12), count: 2),spacing: 12, pinnedViews: [.sectionHeaders]) {
            Section(
                header: HStack(spacing: 12) {
                    AppFilterButton(state.filterType.toText(), icon: .icArrowUpDown, action: onOpenFilterTypeClick)
                    AppFilterButton(state.sortType.toText(), icon: .icArrowDown, action: onOpenSortTypeClick)
                }.frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.bottom, 8)
                    .background(.white)
            ) {
                ForEach(state.albumsWithSummary) { albumWitSummary in
                    AlbumCard(
                        album: albumWitSummary.album,
                        isSmallMode: true,
                        summary: albumWitSummary.summary,
                        onClick: {
                            onAlbumClick(albumWitSummary.album)
                        }
                    )
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 60)
    }
}

private extension HomeViewModel.AlbumFilterType {
    func getEmptyViewTitle() -> LocalizedStringKey {
        switch self {
        case .daily:
            return "No daily album."
        case .weekly:
            return "No weekly album."
        case .monthly:
            return "No monthly album."
        default:
            return "No albums yet."
        }
    }

    func getEmptyViewDescription() -> LocalizedStringKey {
        switch self {
        case .daily:
            return "When you create daily albums, they will appear here."
        case .weekly:
            return "When you create weekly albums, they will appear here."
        case .monthly:
            return "When you create monthly albums, they will appear here."
        default:
            return "Start tracking your progress by creating your first photo album."
        }
    }

    func toText() -> LocalizedStringKey {
        switch self {
        case .daily:
            return "Daily"
        case .weekly:
            return "Weekly"
        case .monthly:
            return "Monthly"
        default:
            return "All"
        }
    }
}

private extension HomeViewModel.AlbumSortType {
    func toText() -> LocalizedStringKey {
        switch self {
        case .date:
            return "Start date"
        case .title:
            return "Title"
        default:
            return ""
        }
    }
}

private extension HomeViewModel.HomeContentMode {
    func toIcon() -> ImageResource {
        switch self {
        case .list: .icGrid
        default: .icList
        }
    }
}
