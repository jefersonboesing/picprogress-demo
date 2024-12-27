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

struct AppIconButton: View {
    let icon: ImageResource
    let type: AppButtonType
    let action: () -> Void

    init(
        _ icon: ImageResource,
        type: AppButtonType = .primary,
        action: @escaping () -> Void
    ) {
        self.icon = icon
        self.type = type
        self.action = action
    }
    
    var body: some View {
        let buttonColors = type.getButtonColors()
        Button(action: action) {
            Image(icon)
                .renderingMode(.template)
                .frame(width: 56, height: 56)
                .background(buttonColors.containerColor)
                .accentColor(buttonColors.contentColor)
                .cornerRadius(12)
        }
    }
}
