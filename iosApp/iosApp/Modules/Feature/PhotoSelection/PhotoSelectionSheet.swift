//
//  PhotoSelectionSheet.swift
//  PicProgress
//
//  Created by Jeferson on 16/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoSelectionSheet: View {
    
    @StateObject
    private var viewModel: PhotoSelectionViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<PhotoSelectionViewModel.State> = StateWrapper(Defaults().statePhotoSelection())
    
    let goBack: () -> Void
    let goBackWithResult: (AppNavigation.DestinationResult) -> Void
    
    init(
        album: Album,
        initialSelection: [Photo],
        unavailablePhotos: [Photo],
        minRequired: Int32,
        goBack: @escaping () -> Void,
        goBackWithResult: @escaping (AppNavigation.DestinationResult) -> Void
    ) {
        self._viewModel = StateObject(wrappedValue: Injection().photoSelectionViewModel(album: album, initialSelection: initialSelection, unavailablePhotos: unavailablePhotos, minRequired: minRequired))
        self.goBackWithResult = goBackWithResult
        self.goBack = goBack
    }

    var body: some View {
        PhotoSelectionScreen(
            state: stateWrapper.value,
            onApplyClick: viewModel.onApplyClick,
            onPhotoClick: viewModel.onPhotoClick(photo:),
            onCloseClick: goBack
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case let _ as PhotoSelectionViewModel.ActionClose:
                goBack()
                return
            case let action as PhotoSelectionViewModel.ActionReturnPhotoSelection:
                goBackWithResult(.photoSelection(selectedPhotos: action.photos))
                return
            default:
                return
            }
        }
    }
}

private struct PhotoSelectionScreen: View {
    let state: PhotoSelectionViewModel.State
    let onApplyClick: () -> Void
    let onPhotoClick: (Photo) -> Void
    let onCloseClick: () -> Void
    
    var body: some View {
        NavigationView {
            VStack {
                if state.photos.isEmpty {
                    AppEmptyView(
                        image: .icNoPictures,
                        title: "No pictures available.",
                        description: "Capture or import more photos to be able to compare side by side."
                    )
                } else {
                    Gallery(photos: state.photos, selectedPhotos: state.selectedPhotos, onPhotoClick: onPhotoClick, showLabel: false)
                }
                Spacer()
                if state.isReady {
                    AppButton("Apply", type: .primary, action: onApplyClick).padding(16)
                } else if !state.photos.isEmpty {
                    Text("Select \(state.minRequired) photos to compare").font(.bodyMedium()).padding(16)
                }
            }
            .appToolbar(
                title: "Select photo",
                titlePlacement: .principal,
                leadingAction: AppToolbarAction(icon: .icArrowDown1, action: onCloseClick)
            )
        }
    }
}

