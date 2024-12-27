//
//  PreviewView.swift
//  PicProgress
//
//  Created by Jeferson on 11/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PreviewView: View {
    
    @StateObject
    private var viewModel: PreviewViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<PreviewViewModel.State> = StateWrapper(Defaults().statePreview())
    
    @State
    private var toastMessage: AppToastMessage? = nil
    
    let goBack: () -> Void
    let popTo: (Int) -> Void
    
    init(photoPath: PhotoPath, album: Album, goBack: @escaping () -> Void, popTo: @escaping (Int) -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().previewViewModel(photoPath: photoPath, album: album))
        self.goBack = goBack
        self.popTo = popTo
    }

    var body: some View {
        PreviewScreen(
            state: stateWrapper.value,
            onCloseClick: viewModel.onCloseClick,
            onChangeCompareOpacityClick: viewModel.onChangeCompareOpacityClick,
            onAddPhotoClick: viewModel.onAddPhotoClick,
            onBackToCameraClick: viewModel.onBackToCameraClick
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case _ as PreviewViewModel.ActionClose: popTo(2)
            case let _ as PreviewViewModel.ActionGoBack: goBack()
            case let action as PreviewViewModel.ActionShowToast:
                toastMessage = createAppToastMessage(message: action.toastMessage, type: action.toastType)
            default: return
            }
        }
        .onAppear {
            viewModel.initialize()
        }
        .appToast(message: $toastMessage)
    }
}

private struct PreviewScreen: View {
    
    let state: PreviewViewModel.State
    let onCloseClick: () -> Void
    let onChangeCompareOpacityClick: () -> Void
    let onAddPhotoClick: () -> Void
    let onBackToCameraClick: () -> Void
    
    let previewPhoto: Photo

    init(state: PreviewViewModel.State, onCloseClick: @escaping () -> Void, onChangeCompareOpacityClick: @escaping () -> Void, onAddPhotoClick: @escaping () -> Void, onBackToCameraClick: @escaping () -> Void) {
        self.state = state
        self.onCloseClick = onCloseClick
        self.onChangeCompareOpacityClick = onChangeCompareOpacityClick
        self.onAddPhotoClick = onAddPhotoClick
        self.onBackToCameraClick = onBackToCameraClick
        self.previewPhoto = Defaults().photo(photoPath: state.photoPath)
    }
    
    var body: some View {
        VStack {
            ZStack(alignment: .bottomTrailing) {
                PhotoItem(photo: previewPhoto).padding(.top, 12)
                if let lastPhoto = state.lastPhoto, state.isOpacityChecked == true {
                    PhotoItem(photo: lastPhoto).opacity(0.4)
                }
                AppToggleButton(
                    state.isOpacityChecked ? .icSubtract : .icCombine,
                    checked: state.isOpacityChecked,
                    enabled: state.lastPhoto != nil,
                    action: onChangeCompareOpacityClick
                )
                .padding(18)
            }

            Spacer()
            HStack {
                AppIconButton(.icArrowLeft, type: .secondary, action: onBackToCameraClick)
                AppButton("Add to album", type: .primary, action: onAddPhotoClick)
            }
            .frame(maxWidth: .infinity)
            .padding(16)
        }
        .appToolbar(
            title: "",
            leadingAction: AppToolbarAction(
                icon: .icClose,
                action: onCloseClick
            )
        )
    }
    
}
