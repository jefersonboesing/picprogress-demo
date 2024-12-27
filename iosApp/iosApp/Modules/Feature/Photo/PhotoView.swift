//
//  PhotoView.swift
//  PicProgress
//
//  Created by Jeferson on 16/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoView: View {
    
    @EnvironmentObject
    private var appNavigation: AppNavigation
    
    @StateObject
    private var viewModel: PhotoViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<PhotoViewModel.State> = StateWrapper(Defaults().statePhoto())
    
    @State
    private var toastMessage: AppToastMessage? = nil
    
    let navigateTo: (AppNavigation.Destination) -> Void
    let goBack: () -> Void
    
    init(album: Album, photo: Photo, navigateTo: @escaping (AppNavigation.Destination) -> Void, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().photoViewModel(album: album, photo: photo))
        self.navigateTo = navigateTo
        self.goBack = goBack
    }

    var body: some View {
        PhotoScreen(
            state: stateWrapper.value,
            onBackClick: viewModel.onBackClick,
            onCompareClick: viewModel.onCompareClick,
            onOptionsClick: viewModel.onOptionsClick,
            onEditDateClick: viewModel.onEditDateClick,
            onDeleteClick: viewModel.onDeleteClick,
            onOptionsHidden: viewModel.onOptionsHidden,
            onDeleteConfirmClick: viewModel.onDeleteConfirmClick,
            onConfirmationClose: viewModel.onConfirmationClose
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case _ as PhotoViewModel.ActionGoBack: goBack()
            case let action as PhotoViewModel.ActionGoToCompare: navigateTo(.page(.compare(album: action.album, comparePhotos: action.comparePhotos)))
            case let action as PhotoViewModel.ActionGoToEdit: navigateTo(.sheet(.photoConfig(photo: action.photo)))
            case let action as PhotoViewModel.ActionGoToPhotoSelection: navigateTo(.sheet(.photoSelection(album: action.album, initialSelection: [], unavailablePhotos: action.unavailablePhotos, minRequired: 1)))
            case let action as PhotoViewModel.ActionShowToast:
                toastMessage = createAppToastMessage(message: action.toastMessage, type: action.toastType)
            default: return
            }
        }
        .onAppear {
            viewModel.onRefresh()
        }
        .onChange(of: appNavigation.navigationResult) { newValue in
            switch newValue {
            case .photoSelection(let selectedPhotos):
                if let photoToCompareWith = selectedPhotos.first {
                    viewModel.onComparePhotoSelectionChange(photoToCompareWith: photoToCompareWith)
                }
            default: break
            }
            appNavigation.clearNavigationResult()
        }
        .onChange(of: appNavigation.activeSheet) { newValue in
            if newValue == nil { viewModel.onRefresh() }
        }
        .appToast(message: $toastMessage)
    }
}

private struct PhotoScreen: View {
    
    let state: PhotoViewModel.State
    let onBackClick: () -> Void
    let onCompareClick: () -> Void
    let onOptionsClick: () -> Void
    let onEditDateClick: () -> Void
    let onDeleteClick: () -> Void
    let onOptionsHidden: () -> Void
    let onDeleteConfirmClick: () -> Void
    let onConfirmationClose: () -> Void
    
    var body: some View {
        VStack {
            PhotoItem(photo: state.photo).padding(.top, 12)
            Spacer()
            PhotoDate(album: state.album, photo: state.photo)
            AppButton("Compare", type: .primary, startIcon: .icCompare, action: onCompareClick)
                .padding(.horizontal, 16)
                .padding(.bottom, 16)
        }
        .appToolbar(
            title: "Photo",
            leadingAction: AppToolbarAction(
                icon: .icArrowLeft,
                action: onBackClick
            ),
            trailingActions: [
                AppToolbarAction(
                    icon: .icMoreHorizontal,
                    action: onOptionsClick
                )
            ]
        )
        .appOptionsSheet(
            isPresented: state.isMoreOptionsVisible,
            title: "More options",
            options: [
                AppOption(
                    text: "Edit date",
                    icon: .icEdit,
                    onClick: onEditDateClick
                ),
                AppOption(
                    text: "Delete",
                    icon: .icTrash,
                    onClick: onDeleteClick
                )
            ],
            onDismiss: onOptionsHidden
        )
        .appDialog(
            icon: .icTrashDialog,
            title: "Are you sure you want to delete this photo?",
            message: "After deleting, it will no longer be possible to access them.",
            positiveButtonText: "Delete",
            negativeButtonText: "Cancel",
            onPositiveButtonClick:  onDeleteConfirmClick,
            onNegativeButtonClick: onConfirmationClose,
            isVisible: state.isDeleteConfirmationVisible
        )
    }
    
}
