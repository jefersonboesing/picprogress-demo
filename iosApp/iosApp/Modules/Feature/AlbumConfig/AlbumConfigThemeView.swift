//
//  AlbumConfigThemeView.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumConfigThemeView: View {
    
    @EnvironmentObject 
    private var sharedViewModel: AlbumConfigViewModel
    
    @StateObject
    private var viewModel: AlbumConfigThemeViewModel
    
    @StateObject
    private var stateWrapper = StateWrapper<AlbumConfigThemeViewModel.State>(Defaults().stateAlbumConfigTheme())
    
    let goBack: () -> Void
    
    init(currentTheme: AlbumTheme, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().albumConfigThemeViewModel(theme: currentTheme))
        self.goBack = goBack
    }

    var body: some View {
        AlbumConfigThemeScreen(
            state: stateWrapper.value,
            onBackClick: viewModel.onBackClick,
            onSelectedThemeChange: viewModel.onSelectedAlbumThemeChange(albumTheme:),
            onApplyClick: viewModel.onApplyClicked
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch(action) {
            case _ as AlbumConfigThemeViewModel.ActionGoBack:
                goBack()
                return
            case let action as AlbumConfigThemeViewModel.ActionApplyAlbumTheme:
                sharedViewModel.albumTheme = action.theme
                goBack()
                return
            default:
                return
            }
        }
    }
}

private struct AlbumConfigThemeScreen: View {
    let state: AlbumConfigThemeViewModel.State
    let onBackClick: () -> Void
    let onSelectedThemeChange: (AlbumTheme) -> Void
    let onApplyClick: () -> Void

    var body: some View {
        VStack(spacing: 0){
            ForEach(state.theme, id: \.self) { theme in
                ListItem(
                    label: LocalizedStringKey(theme.name),
                    startContent: {
                        AnyView(
                            Circle()
                                .fill(theme.asPrimaryColor())
                                .frame(width: 40, height: 40)
                        )
                    },
                    selected: state.selectedTheme == theme,
                    onClick: { onSelectedThemeChange(theme) }
                )
            }

            Spacer()

            AppButton("Apply", action: onApplyClick)
            .frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 16)
        .padding(.top, 8)
        .background(Color.white)
        .appToolbar(
            title: "Background color",
            titlePlacement: .principal,
            leadingAction: AppToolbarAction(icon: .icArrowLeft, action: onBackClick)
        )
    }
}
