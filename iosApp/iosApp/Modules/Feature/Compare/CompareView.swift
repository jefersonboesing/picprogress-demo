//
//  CompareView.swift
//  PicProgress
//
//  Created by Jeferson on 16/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CompareView: View {
        
    @EnvironmentObject
    private var appNavigation: AppNavigation
    
    @StateObject
    private var viewModel: CompareViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<CompareViewModel.State> = StateWrapper(Defaults().stateCompare())
    
    @State
    private var toastMessage: AppToastMessage? = nil
    
    let navigateTo: (AppNavigation.Destination) -> Void
    let goBack: () -> Void
    
    init(
        album: Album,
        comparePhotos: ComparePhotos?,
        navigateTo: @escaping (AppNavigation.Destination) -> Void,
        goBack: @escaping () -> Void
    ) {
        self._viewModel = StateObject(wrappedValue: Injection().compareViewModel(album: album, comparePhotos: comparePhotos))
        self.navigateTo = navigateTo
        self.goBack = goBack
    }

    var body: some View {
        CompareScreen(
            state: stateWrapper.value,
            onBackClick: viewModel.onBackClicked,
            onChangePhotosClick: viewModel.onChangePhotosClick,
            onCompareModeChange: viewModel.onCompareModeChange(compareMode:),
            onOverModeHold: viewModel.onOverModeHold
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case _ as CompareViewModel.ActionGoBack: goBack()
            case let action as CompareViewModel.ActionGoToPhotoSelection:
                navigateTo(.sheet(.photoSelection(album: action.album, initialSelection: action.initialSelection, unavailablePhotos: [], minRequired: 2)))
            case let action as CompareViewModel.ActionShowToast:
                toastMessage = createAppToastMessage(message: action.toastMessage, type: action.toastType)
                break
            default: return
            }
        }
        .onChange(of: appNavigation.navigationResult) { newValue in
            switch newValue {
            case .photoSelection(let selectedPhotos):
                if selectedPhotos.count == 2 {
                    viewModel.onComparePhotoSelectionChange(photos: selectedPhotos)
                }
            default: break
            }
            appNavigation.clearNavigationResult()
        }
        .appToast(message: $toastMessage)
    }
}

private struct CompareScreen: View {
    
    var state: CompareViewModel.State
    var onBackClick: () -> Void
    var onChangePhotosClick: () -> Void
    var onCompareModeChange: (CompareViewModel.CompareMode) -> Void
    var onOverModeHold: () -> Void
    
    var body: some View {
        ZStack {
            VStack {
                if let comparePhotos = state.comparePhotos {
                    if state.compareMode == .sidebyside {
                        SideBySideMode(comparePhotos: comparePhotos, onChangePhotosClick: onChangePhotosClick)
                    } else {
                        OverMode(comparePhotos: comparePhotos, onOverModeHold: onOverModeHold)
                    }
                    CompareModeTab(currentMode: state.compareMode, onCompareModeChange: onCompareModeChange)
                    .frame(maxWidth: .infinity)
                    .padding(16)
                } else {
                    PhotoComparePlaceholder(onAddPhotosClick: onChangePhotosClick)
                    Spacer()
                    Text("No photos selected")
                        .font(.bodyMedium())
                        .padding(16)
                }
            }
            .animation(.spring, value: state.compareMode)
            .frame(maxHeight: .infinity)
            
            if (state.compareMode == .over && !state.isCompareOverTutorialViewed) {
                TutorialScreen(
                    icon: .icTutorialHold,
                    message: "Hold to compare before and after"
                )
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .appToolbar(
            title: "Compare",
            leadingAction: AppToolbarAction(
                icon: .icArrowLeft,
                action: onBackClick
            )
        )
    }
}

private struct PhotoComparePlaceholder: View {
    
    var onAddPhotosClick: () -> Void
    
    var body: some View {
        VStack {
            ZStack(alignment: .center) {
                HStack(spacing: 12) {
                    PhotoPlaceholderBox(onClick: onAddPhotosClick)
                    PhotoPlaceholderBox(onClick: onAddPhotosClick)
                }
                AppFloatingActionButton(.icPlus, action: onAddPhotosClick)
            }
            .padding(.horizontal, 16)
            HStack(spacing: 12) {
                PhotoLabel(title: "Before")
                PhotoLabel(title: "After")
            }
            .padding(.vertical, 24)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .aspectRatio(3 / 4, contentMode: .fit)
        .padding(.top, 12)
    }
}

private struct SideBySideMode: View {
    
    var comparePhotos: ComparePhotos
    var onChangePhotosClick: () -> Void
    
    var body: some View {
        VStack {
            ZStack(alignment: .center) {
                HStack(spacing: 12) {
                    PhotoItem(
                        photo: comparePhotos.beforePhoto,
                        onLeftTap: onChangePhotosClick,
                        onRightTap: onChangePhotosClick,
                        shape: RoundedRectangle(cornerRadius: 8)
                    )
                    PhotoItem(
                        photo: comparePhotos.afterPhoto,
                        onLeftTap: onChangePhotosClick,
                        onRightTap: onChangePhotosClick,
                        shape: RoundedRectangle(cornerRadius: 8)
                    )
                }
                .padding(.horizontal, 16)
                AppFloatingActionButton(.icRotate, action: onChangePhotosClick)
            }
            HStack(spacing: 12) {
                PhotoLabel(title: "Before", photo: comparePhotos.beforePhoto)
                PhotoLabel(title: "After", photo: comparePhotos.afterPhoto)
            }
            .padding(.vertical, 24)
            .padding(.horizontal, 16)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .aspectRatio(3 / 4, contentMode: .fit)
        .padding(.top, 12)
    }
}

private struct OverMode: View {
            
    var comparePhotos: ComparePhotos
    var onOverModeHold: () -> Void
    
    @State
    private var currentPhoto: Photo
    
    init(comparePhotos: ComparePhotos, onOverModeHold: @escaping () -> Void) {
        self.comparePhotos = comparePhotos
        self.onOverModeHold = onOverModeHold
        self.currentPhoto = comparePhotos.afterPhoto
    }
    
    var body: some View {
        ZStack(alignment: .topTrailing) {
            PhotoItem(
                photo: self.currentPhoto,
                onPress: {
                    self.currentPhoto = comparePhotos.beforePhoto
                    onOverModeHold()
                },
                onRelease: {
                    self.currentPhoto = comparePhotos.afterPhoto
                }
            )
            Image(.appLogoLight).padding(16)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .aspectRatio(3 / 4, contentMode: .fit)
        .padding(.top, 12)
    }
}



private struct PhotoPlaceholderBox: View {
    var onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            Image(.icPhoto)
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .foregroundColor(.custom(.PrimaryLight()))
                .padding()
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(style: StrokeStyle(lineWidth: 2, dash: [5, 5]))
                        .foregroundColor(.custom(.PrimaryLight()))
                )
                .aspectRatio(3 / 4, contentMode: .fit)
        }
    }
}

private struct CompareModeTab: View {
    private let modes: [CompareViewModel.CompareMode] = [.sidebyside, .over]
    private let iconModes: [AppIconTab] = [AppIconTab(icon: .icSideBySide), AppIconTab(icon: .icMagicWand)]
    var currentMode: CompareViewModel.CompareMode
    var onCompareModeChange: (CompareViewModel.CompareMode) -> Void

    var body: some View {
        AppTab(
            tabs: iconModes,
            selectedIndex: modes.firstIndex(of: currentMode) ?? 0) { 
                tabIndex in onCompareModeChange(modes[tabIndex])
            }
        .padding(.top, 24)
    }
}
