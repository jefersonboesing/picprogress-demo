//
//  AppButton.swift
//  PicProgress
//
//  Created by Jeferson on 19/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

import SwiftUI
import shared

struct AppButton: View {
    let text: LocalizedStringKey
    let type: AppButtonType
    let startIcon: ImageResource?
    let endIcon: ImageResource?
    let action: () -> Void

    init(
        _ text: LocalizedStringKey,
        type: AppButtonType = .primary,
        startIcon: ImageResource? = nil,
        endIcon: ImageResource? = nil,
        action: @escaping () -> Void
    ) {
        self.text = text
        self.type = type
        self.startIcon = startIcon
        self.endIcon = endIcon
        self.action = action
    }
    
    var body: some View {
        let buttonColors = type.getButtonColors()
        Button(action: action) {
            HStack(spacing: 0) {
                if let startIcon = startIcon {
                    Image(startIcon).renderingMode(.template)
                }
                Text(text).font(.titleMedium()).padding(.horizontal, 8)
                if let endIcon = endIcon {
                    Image(endIcon).renderingMode(.template)
                }
            }
            .accentColor(buttonColors.contentColor)
            .padding(.horizontal, 24)
            .padding(.vertical, 16)
            .frame(maxWidth: .infinity, minHeight: 56, maxHeight: 56)
            .background(buttonColors.containerColor)
            .cornerRadius(10)
        }
    }
}
