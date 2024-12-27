//
//  ToolbarExtension.swift
//  PicProgress
//
//  Created by Jeferson on 19/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

extension View {
    
    func appToolbar(
        title: String,
        titlePlacement: ToolbarItemPlacement = .topBarLeading,
        titleFont: Font = .titleMedium(),
        leadingAction: AppToolbarAction? = nil,
        trailingActions: [AppToolbarAction] = [],
        colorScheme: ColorScheme = .light,
        localizeTitle: Bool = true
    ) -> some View {
        let foregroundColor: Color = colorScheme == .light ? .custom(.NeutralLowDarkest()) : .custom(.NeutralHighLightest())
        return toolbar {
            if let leadingAction {
                ToolbarItem(placement: .topBarLeading) {
                    Button(action: leadingAction.action) {
                        Image(leadingAction.icon)
                            .renderingMode(.template)
                            .foregroundColor(foregroundColor)
                            .padding(6)
                    }.buttonStyle(.plain)
                }
            }
            ToolbarItem(placement: titlePlacement) {
                if localizeTitle {
                    Text(LocalizedStringKey(title))
                        .font(titleFont)
                        .foregroundColor(foregroundColor)
                } else {
                    Text(title)
                        .font(titleFont)
                        .foregroundColor(foregroundColor)
                }
            }
            ToolbarItem(placement: .topBarTrailing) {
                HStack(spacing: 0) {
                    ForEach(trailingActions) { action in
                        Button(action: action.action) {
                            Image(action.icon)
                                .renderingMode(.template)
                                .foregroundColor(foregroundColor)
                                .padding(12)
                        }.buttonStyle(.plain)
                    }
                }
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbarBackground(.white, for: .navigationBar)
        .toolbarBackground(.hidden, for: .navigationBar)
        .toolbarColorScheme(colorScheme, for: .navigationBar)
        
    }
    
}

struct AppToolbarAction: Identifiable {
    let id = UUID()
    let icon: ImageResource
    let action: () -> Void
}

enum AppToolbarColorScheme {
    case light
    case dark
}
