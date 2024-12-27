//
//  AlbumView.swift
//  PicProgress
//
//  Created by Jeferson on 02/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import PhotosUI

struct AlbumView: View {
    
    @EnvironmentObject
    private var appNavigation: AppNavigation
    
    @StateObject
    private var viewModel: AlbumViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<AlbumViewModel.State> = StateWrapper(Defaults().stateAlbum())
    
    let navigateTo: (AppNavigation.Destination) -> Void
    let goBack: () -> Void
    
    @State private var isPhotoPickerPresented = false
    @State private var selectedItems: [PhotosPickerItem] = []
    @State private var toastMessage: AppToastMessage? = nil
    @State private var isImporting = false
    @State private var progressState: ProgressState = .indeterminate
    
    init(album: Album, navigateTo: @escaping (AppNavigation.Destination) -> Void, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().albumViewModel(album: album))
        self.navigateTo = navigateTo
        self.goBack = goBack
    }
    
    var body: some View {
        VStack {
            if case .none = progressState {
                AlbumScreen(
                    state: stateWrapper.value,
                    onBackClick: viewModel.onBackClick,
                    onAddPhotoClick: viewModel.onAddPhotoClick,
                    onTakePictureClick: viewModel.onTakePictureClick,
                    onUploadFromGalleryClick: viewModel.onUploadFromGalleryClick,
                    onMoreOptionsClick: viewModel.onMoreOptionsClick,
                    onSelectPhotosClick: viewModel.onSelectPhotosClick,
                    onEditAlbumClick: viewModel.onEditAlbumClick,
                    onDeleteAlbumClick: viewModel.onDeleteAlbumClick,
                    onPhotoClick: viewModel.onPhotoClick(photo:),
                    onOptionsHidden: viewModel.onOptionsHidden,
                    onDeletePhotosClick: viewModel.onDeletePhotosClick,
                    onCancelSelectionClick: viewModel.onCancelSelectionClick,
                    onCompareClick: viewModel.onCompareClick,
                    onDeletePhotosConfirmClick: viewModel.onDeletePhotosConfirmClick,
                    onDeleteAlbumConfirmClick: viewModel.onDeleteAlbumConfirmClick
                )
                .appActionChange(viewModel: viewModel) { action in
                    switch action {
                    case _ as AlbumViewModel.ActionClose: goBack()
                    case let action as AlbumViewModel.ActionGoToAlbumEdit: navigateTo(.sheet(.albumConfig(album: action.album)))
                    case let action as AlbumViewModel.ActionGoToCamera: navigateTo(.page(.camera(album: action.album)))
                    case let action as AlbumViewModel.ActionGoToPhoto: navigateTo(.page(.photo(album: action.album, photo: action.photo)))
                    case let action as AlbumViewModel.ActionGoToCompare: navigateTo(.page(.compare(album: action.album, comparePhotos: nil)))
                    case _ as AlbumViewModel.ActionImportPhotoFromGallery: isPhotoPickerPresented = true
                    case let action as AlbumViewModel.ActionShowToast:
                        toastMessage = createAppToastMessage(message: action.toastMessage, type: action.toastType)
                    default: return
                    }
                }
                .transition(.opacity)
            } else {
                LoadingScreen(state: $progressState)
            }
        }
        .appStateChange(from: viewModel, into: stateWrapper)
        .onAppear {
            viewModel.onRefresh()
        }
        .onChange(of: stateWrapper.value) { newValue in
            if (!isImporting) {
                withAnimation {
                    progressState = newValue.isLoading ? .indeterminate : .none
                }
            }
        }
        .photosPicker(
            isPresented: $isPhotoPickerPresented,
            selection: $selectedItems,
            matching: .all(of: [.images, .not(.screenshots)])
        )
        .onChange(of: selectedItems) { items in
            if !items.isEmpty {
                Task {
                    isImporting = true
                    for (index, item) in items.enumerated() {
                        if let url = await item.saveToTemporaryFile() {
                            viewModel.onPhotoImported(photoPath: PhotoPath(path: url.path(percentEncoded: true)))
                        }
                        progressState = .determinate(current: index, total: items.count, message: "Importing photos...")
                    }
                    withAnimation {
                        isImporting = false
                        progressState = .none
                    }
                }
                selectedItems = []
            }
        }
        .onChange(of: appNavigation.activeSheet) { newValue in
            if newValue == nil { viewModel.onRefresh() }
        }
        .appToast(message: $toastMessage)
    }
}

private struct AlbumScreen: View {
    
    let state: AlbumViewModel.State
    let onBackClick: () -> Void
    let onAddPhotoClick: () -> Void
    let onTakePictureClick: () -> Void
    let onUploadFromGalleryClick: () -> Void
    let onMoreOptionsClick: () -> Void
    let onSelectPhotosClick: () -> Void
    let onEditAlbumClick: () -> Void
    let onDeleteAlbumClick: () -> Void
    let onPhotoClick: (Photo) -> Void
    let onOptionsHidden: () -> Void
    let onDeletePhotosClick: () -> Void
    let onCancelSelectionClick: () -> Void
    let onCompareClick: () -> Void
    let onDeletePhotosConfirmClick: () -> Void
    let onDeleteAlbumConfirmClick: () -> Void
    
    
    var body: some View {
        let toolbarActions = [AppToolbarAction(icon: .icMoreHorizontal, action: onMoreOptionsClick)]
        
        let selectionActions = [AppToolbarAction(icon: .icClose, action: onCancelSelectionClick)]
        
        VStack {
            if state.isEmptyViewVisible {
                AppEmptyView(
                    image: .icNoPictures,
                    title: "No pictures yet.",
                    description: "Capture new photos or import from gallery to track the progress."
                )
            } else {
                Gallery(photos: state.photos, selectedPhotos: state.selectedPhotos, onPhotoClick: onPhotoClick)
            }
            if state.isSelectionEnabled {
                if !state.selectedPhotos.isEmpty {
                    selectionBottomView
                }
            } else {
                defaultBottomView
            }
        }
        .animation(.easeInOut, value: state.selectedPhotos)
        .animation(.easeInOut, value: state.isSelectionEnabled)
        .appToolbar(
            title: state.title,
            leadingAction: AppToolbarAction(icon: .icArrowLeft, action: onBackClick),
            trailingActions: state.isSelectionEnabled ? selectionActions : toolbarActions,
            localizeTitle: false
        )
        .appOptionsSheet(
            isPresented: state.isMoreOptionsVisible,
            title: "More options",
            options: (
                (state.photos.isEmpty ? [] : [
                    AppOption(
                        text: "Select photos",
                        icon: .icSelect,
                        onClick: onSelectPhotosClick
                    )
                ]) + [
                    AppOption(
                        text: "Edit album",
                        icon: .icEdit,
                        onClick: onEditAlbumClick
                    ),
                    AppOption(
                        text: "Delete",
                        icon: .icTrash,
                        onClick: onDeleteAlbumClick
                    )
                ]
                
            ),
            onDismiss: onOptionsHidden
        )
        .appOptionsSheet(
            isPresented: state.isAddPhotoOptionsVisible,
            title: "Add photo",
            options: [
                AppOption(
                    text: "Take picture",
                    icon: .icCamera,
                    onClick: onTakePictureClick
                ),
                AppOption(
                    text: "Import from gallery",
                    icon: .icUpload,
                    onClick: onUploadFromGalleryClick
                ),
            ],
            onDismiss: onOptionsHidden
        )
        .appDialog(
            icon: .icTrashDialog,
            title: "Are you sure you want to delete \(state.selectedPhotos.count) photo(s)?",
            message: "After deleting, it will no longer be possible to access them.",
            positiveButtonText: "Delete",
            negativeButtonText: "Cancel",
            onPositiveButtonClick:  onDeletePhotosConfirmClick,
            onNegativeButtonClick: onOptionsHidden,
            isVisible: state.isPhotoDeleteConfirmationVisible
        )
        .appDialog(
            icon: .icTrashDialog,
            title: "Are you sure you want to delete this album?",
            message: "After deleting, it will no longer be possible to access it.",
            positiveButtonText: "Delete",
            negativeButtonText: "Cancel",
            onPositiveButtonClick:  onDeleteAlbumConfirmClick,
            onNegativeButtonClick: onOptionsHidden,
            isVisible: state.isAlbumDeleteConfirmationVisible
        )
    }
    
    private var selectionBottomView: some View {
        HStack(spacing: 8) {
            Text("\(state.selectedPhotos.count) photos selected")
                .font(.titleMedium())
                .frame(maxWidth: .infinity)
                .padding(.leading, 64)
            AppIconButton(.icTrash, type: .secondary, action: onDeletePhotosClick)
        }
        .frame(maxWidth: .infinity)
        .padding(16)
    }
    
    private var defaultBottomView: some View {
        HStack {
            AppButton("Add photo", action: onAddPhotoClick)
            if (state.isCompareActionEnabled) {
                AppIconButton(.icCompare, type: .secondary, action: onCompareClick)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(16)
    }
}
