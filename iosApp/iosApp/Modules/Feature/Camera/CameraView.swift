//
//  CameraView.swift
//  PicProgress
//
//  Created by Jeferson on 23/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CameraView: View {
    
    @StateObject
    private var cameraControl = CameraControl()
    
    @StateObject
    private var viewModel: CameraViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<CameraViewModel.State> = StateWrapper(Defaults().stateCamera())
    
    let navigateTo: (AppNavigation.Destination) -> Void
    let goBack: () -> Void
    
    init(album: Album, navigateTo: @escaping (AppNavigation.Destination) -> Void, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().cameraViewModel(album: album))
        self.navigateTo = navigateTo
        self.goBack = goBack
    }

    var body: some View {
        CameraScreen(
            state: stateWrapper.value,
            onPhotoTaken: viewModel.onPhotoTaken(photoPath:),
            onChangeFlashModeClick: viewModel.onChangeFlashModeClick,
            onChangeCameraLensClick: viewModel.onChangeCameraLensClick,
            onCloseClick: viewModel.onCloseClick,
            onChangeCompareOpacityClick: viewModel.onChangeCompareOpacityClick,
            onCameraFailed: viewModel.onCameraFailed,
            cameraControl: cameraControl
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case _ as CameraViewModel.ActionClose:
                goBack()
            case let action as CameraViewModel.ActionGoToPreview:
                navigateTo(.page(.preview(album: action.album, photoPath: action.photoPath)))
            default: return
            }
        }
        .onDisappear {
            cameraControl.releaseSession()
        }
        .onAppear {
            viewModel.initialize()
        }
    }
}

private struct CameraScreen: View {
    
    let state: CameraViewModel.State
    let onPhotoTaken: (PhotoPath) -> Void
    let onChangeFlashModeClick: () -> Void
    let onChangeCameraLensClick: () -> Void
    let onCloseClick: () -> Void
    let onChangeCompareOpacityClick: () -> Void
    let onCameraFailed: () -> Void
    @ObservedObject var cameraControl: CameraControl
    
    init(state: CameraViewModel.State, onPhotoTaken: @escaping (PhotoPath) -> Void, onChangeFlashModeClick: @escaping () -> Void, onChangeCameraLensClick: @escaping () -> Void, onCloseClick: @escaping () -> Void, onChangeCompareOpacityClick: @escaping () -> Void, onCameraFailed: @escaping () -> Void, cameraControl: CameraControl) {
        self.state = state
        self.onPhotoTaken = onPhotoTaken
        self.onChangeFlashModeClick = onChangeFlashModeClick
        self.onChangeCameraLensClick = onChangeCameraLensClick
        self.onCloseClick = onCloseClick
        self.onChangeCompareOpacityClick = onChangeCompareOpacityClick
        self.onCameraFailed = onCameraFailed
        self.cameraControl = cameraControl
    }
    
    var body: some View {
        VStack {
            ZStack(alignment: .bottomTrailing) {
                Camera(control: cameraControl)
                    .overlay {
                        if let lastPhoto = state.lastPhoto, state.isOpacityChecked == true {
                            PhotoItem(photo: lastPhoto).opacity(0.4)
                        }
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
            Button(action: cameraControl.takePhoto) {
                Image(.icShutter)
            }.padding(16)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(.black)
        .appToolbar(
            title: "Camera",
            leadingAction: AppToolbarAction(
                icon: .icClose,
                action: onCloseClick
            ),
            trailingActions: [
                AppToolbarAction(
                    icon: state.flashMode.toFlashIcon(),
                    action: onChangeFlashModeClick
                ),
                AppToolbarAction(
                    icon: .icRotate,
                    action: onChangeCameraLensClick
                )
            ],
            colorScheme: .dark
        )
        .onChange(of: cameraControl.image) { newValue in
            Task {
                if let photoTaken = newValue, let photo = await photoTaken.saveToTemporaryFileJPEG() {
                    onPhotoTaken(PhotoPath(path: photo.path(percentEncoded: true)))
                }
            }
        }
        .onChange(of: state.flashMode) { newValue in
            cameraControl.setFlashMode(flashMode: newValue)
        }
        .onChange(of: state.cameraLens) { newValue in
            cameraControl.setCameraLens(cameraLens: newValue)
        }
    }
    
}

private extension FlashMode {
    
    func toFlashIcon() -> ImageResource {
        return switch self {
        case .auto_: .icFlashA
        case .off: .icFlashDisabled
        default: .icFlash
        }
    }
    
}
