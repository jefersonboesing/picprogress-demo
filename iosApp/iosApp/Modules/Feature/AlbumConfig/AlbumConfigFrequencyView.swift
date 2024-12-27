//
//  AlbumConfigFrequencyView.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumConfigFrequencyView: View {
    @EnvironmentObject
    private var sharedViewModel: AlbumConfigViewModel
    
    @StateObject
    private var viewModel: AlbumConfigFrequencyViewModel
    
    @StateObject 
    private var stateWrapper: StateWrapper<AlbumConfigFrequencyViewModel.State> = StateWrapper(Defaults().stateAlbumConfigFrequency())
    
    let goBack: () -> Void
    
    init(currentFrequency: Frequency, goBack: @escaping () -> Void) {
        self._viewModel = StateObject(wrappedValue: Injection().albumConfigFrequencyViewModel(frequency: currentFrequency))
        self.goBack = goBack
    }
    
    var body: some View {
        AlbumConfigFrequencyScreen(
            state: stateWrapper.value,
            onBackClick: viewModel.onBackClick,
            onSelectedFrequencyChange: viewModel.onSelectedFrequencyChange(frequency:),
            onApplyClick: viewModel.onApplyClicked
        )
        .appStateChange(from: viewModel, into: stateWrapper)
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case _ as  AlbumConfigFrequencyViewModel.ActionGoBack:
                goBack()
                return
            case let action as AlbumConfigFrequencyViewModel.ActionApplyAlbumFrequency:
                sharedViewModel.frequency = action.frequency
                goBack()
                return
            default:
                return
            }
        }
    }
}

private struct AlbumConfigFrequencyScreen: View {
    let state: AlbumConfigFrequencyViewModel.State
    let onBackClick: () -> Void
    let onSelectedFrequencyChange: (Frequency) -> Void
    let onApplyClick: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            ForEach(state.frequencies, id: \.self) { frequency in
                ListItem(
                    label: frequency.toDescription(),
                    selected: state.selectedFrequency == frequency,
                    onClick: { onSelectedFrequencyChange(frequency) }
                )
            }
            Spacer()
            AppButton("Apply", action: onApplyClick).frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 16)
        .padding(.top, 8)
        .background(.custom(.NeutralHighLightest()))
        .appToolbar(
            title: "Photo frequency",
            titlePlacement: .principal,
            leadingAction: AppToolbarAction(icon: .icArrowLeft, action: onBackClick)
        )
    }
}
