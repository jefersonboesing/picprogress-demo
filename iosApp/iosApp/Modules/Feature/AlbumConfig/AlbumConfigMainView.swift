//
//  AlbumConfigMainView.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumConfigMainView: View {
    @EnvironmentObject
    private var sharedViewModel: AlbumConfigViewModel
    
    @StateObject
    private var viewModel: AlbumConfigMainViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<AlbumConfigMainViewModel.State> = StateWrapper(Defaults().stateAlbumConfigMain())
    
    let navigateTo: (AlbumConfigNavigation.Destination) -> Void
    let goBack: () -> Void
    
    init(album: Album?, navigateTo: @escaping (AlbumConfigNavigation.Destination) -> Void, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().albumConfigMainViewModel(album: album))
        self.navigateTo = navigateTo
        self.goBack = goBack
    }

    var body: some View {
        AlbumConfigMainScreen(
            state: stateWrapper.value,
            onAlbumNameChange: viewModel.onTitleChange(title:),
            onAlbumNotesChange: viewModel.onNotesChange(notes:),
            onGoToChangeThemeClick: viewModel.onGoToChangeThemeClick,
            onGoToChangeFrequencyClick: viewModel.onGoToChangeFrequencyClick,
            onSaveAlbumClick: viewModel.onSaveAlbumClick,
            onGoBackClick: viewModel.onCloseClick
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case let action as AlbumConfigMainViewModel.ActionGoToChangeTheme:
                navigateTo(.page(.theme(current: action.current)))
                return
            case let action as AlbumConfigMainViewModel.ActionGoToChangeFrequency:
                navigateTo(.page(.frequency(current: action.current)))
                return
            case _ as AlbumConfigMainViewModel.ActionClose:
                goBack()
                return
            default:
                return
            }
        }
        .onAppear {
            if let albumTheme = sharedViewModel.albumTheme {
                viewModel.onAlbumThemeChange(albumTheme: albumTheme)
            }
            if let albumFrequency = sharedViewModel.frequency {
                viewModel.onAlbumFrequencyChange(frequency: albumFrequency)
            }
        }
    }
}

private struct AlbumConfigMainScreen: View {
    let state: AlbumConfigMainViewModel.State
    let onAlbumNameChange: (String) -> Void
    let onAlbumNotesChange: (String) -> Void
    let onGoToChangeThemeClick: () -> Void
    let onGoToChangeFrequencyClick: () -> Void
    let onSaveAlbumClick: () -> Void
    let onGoBackClick: () -> Void
    
    var body: some View {
        ZStack {
            ScrollView {
                VStack(spacing: 0) {
                    AlbumCard(
                        album: state.album,
                        isSmallMode: false,
                        summary: state.summary,
                        animate: false,
                        onClick: {}
                    ).padding(.top, 16)

                    AppTextField(
                        label: "Album name",
                        value: state.album.title,
                        onValueChange: onAlbumNameChange,
                        errorText: state.isTitleInvalid ? "Required field." : nil,
                        placeholderText: "Type here the progress name"
                    ).padding(.top, 24)

                    AppTextField(
                        label: "Notes",
                        value: state.album.notes,
                        onValueChange: onAlbumNotesChange,
                        placeholderText: "Write your note",
                        singleLine: false,
                        maxLines: 3,
                        minLines: 3
                    ).padding(.top, 20)
                    
                    Text("Settings")
                        .font(.titleSmall())
                        .foregroundColor(.custom(.NeutralLowLight()))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.top, 24)
                        .padding(.bottom, 8)

                    if state.isNew {
                        ListItem(
                            label: "Photo frequency",
                            endContent: {
                                AnyView(
                                    Text(state.album.frequency.toDescription())
                                        .font(.bodyMedium())
                                        .foregroundColor(.custom(.NeutralLowMedium()))
                                )
                            },
                            endIcon: .icArrowRight,
                            onClick: onGoToChangeFrequencyClick
                        )
                    }
                    
                    ListItem(
                        label: "Background color",
                        endContent: {
                            AnyView(
                            Circle()
                                .fill(state.album.theme.asPrimaryColor())
                                .frame(width: 32, height: 32)
                            )
                        },
                        endIcon: .icArrowRight,
                        onClick: onGoToChangeThemeClick
                    )
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 120)
            }
            .safeAreaInset(edge: .bottom) {
                Color.clear.frame(height: 120)
            }
            .padding(.top, 1)
            VStack {
                Spacer()
                VStack {
                    AppButton(state.isNew ? "Create" : "Save", type: .primary, startIcon: nil, endIcon: nil) {
                        onSaveAlbumClick()
                    }
                }.padding(16).background(.white)
            }
        }
        .dismissKeyboardOnTap()
        .appToolbar(
            title: state.isNew ? "New album" : "Edit album",
            titlePlacement: .principal,
            leadingAction: AppToolbarAction(icon: .icArrowDown1, action: onGoBackClick)
        )
        
    }
}

extension Frequency {
    
    func toDescription() -> LocalizedStringKey {
        switch self {
        case .daily:
            return "Every day"
        case .weekly:
            return "Every week"
        case .monthly:
            return "Every month"
        default:
            return ""
        }
    }
    
}
