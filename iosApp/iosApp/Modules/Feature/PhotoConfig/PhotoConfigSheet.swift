//
//  PhotoConfigSheet.swift
//  PicProgress
//
//  Created by Jeferson on 07/06/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoConfigSheet: View {
    
    @StateObject
    private var viewModel: PhotoConfigViewModel
    
    @StateObject
    private var stateWrapper: StateWrapper<PhotoConfigViewModel.State> = StateWrapper(Defaults().statePhotoConfig())
    
    let goBack: () -> Void
    
    init(
        photo: Photo,
        goBack: @escaping () -> Void
    ) {
        self._viewModel = StateObject(wrappedValue: Injection().photoConfigViewModel(photo: photo))
        self.goBack = goBack
    }

    var body: some View {
        PhotoConfigScreen(
            state: stateWrapper.value,
            onSaveClick: viewModel.onSaveClick,
            onDateChange: viewModel.onDateChanged(date:),
            onCloseClick: goBack
        )
        .appActionChange(viewModel: viewModel) { action in
            switch action {
            case let _ as PhotoConfigViewModel.ActionClose:
                goBack()
                return
            default:
                return
            }
        }
        .appStateChange(from: viewModel, into: stateWrapper)
    }
}

private struct PhotoConfigScreen: View {
    let state: PhotoConfigViewModel.State
    let onSaveClick: () -> Void
    let onDateChange: (Kotlinx_datetimeLocalDate) -> Void
    let onCloseClick: () -> Void
    
    var body: some View {
        NavigationView {
            VStack {
                DateDescription(description: "Original", date: state.originalDate)
                Rectangle()
                    .fill(.custom(.NeutralHighMedium()))
                    .frame(height: 1)
                    .padding(.horizontal, 16)
                DateDescription(description: "Adjusted", date: state.adjustedDate, dateColor: .custom(.NeutralLowDarkest()))
                    .padding(.bottom, 16)
                CalendarView(date: state.adjustedDate.toSwiftDate()) { newDate in
                    onDateChange(newDate.toKotlinxLocalDate())
                }
                .frame(maxHeight: 340)
                .padding(16)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(.custom(.NeutralHighMedium()), lineWidth: 1)
                )
                Spacer()
                AppButton("Save", type: .primary, action: onSaveClick).padding(.vertical, 16)
            }
            .padding(.horizontal, 16)
            .appToolbar(
                title: "Edit date",
                titlePlacement: .principal,
                leadingAction: AppToolbarAction(icon: .icArrowDown1, action: onCloseClick)
            )
        }
    }
}


private struct DateDescription: View {
    let description: LocalizedStringKey
    let date: Kotlinx_datetimeLocalDate
    let dateColor: Color

    init(description: LocalizedStringKey, date: Kotlinx_datetimeLocalDate, dateColor: Color = .custom(.NeutralLowLight())) {
        self.description = description
        self.date = date
        self.dateColor = dateColor
    }

    var body: some View {
        HStack {
            Text(description)
                .font(.titleSmall())
                .frame(maxWidth: .infinity, alignment: .leading)
                .foregroundColor(.custom(.NeutralLowLight()))
            
            Text(date.format())
                .font(.bodyMedium())
                .frame(maxWidth: .infinity, alignment: .trailing)
                .foregroundColor(dateColor)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}
