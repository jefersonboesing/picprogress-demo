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

struct AppToggleButton: View {
    let icon: ImageResource
    let type: AppButtonType
    let checked: Bool
    let enabled: Bool
    let action: () -> Void

    init(
        _ icon: ImageResource,
        type: AppButtonType = .primary,
        checked: Bool,
        enabled: Bool,
        action: @escaping () -> Void
    ) {
        self.icon = icon
        self.type = type
        self.checked = checked
        self.enabled = enabled
        self.action = action
    }
    
    var body: some View {
        let buttonColors = type.getToggleButtonColors(enabled: enabled, checked: checked)
        Button(action: action) {
            Image(icon)
                .renderingMode(.template)
                .padding(12)
                .background(buttonColors.containerColor)
                .accentColor(buttonColors.contentColor)
                .cornerRadius(50)
        }
        .disabled(!enabled)
    }
}
