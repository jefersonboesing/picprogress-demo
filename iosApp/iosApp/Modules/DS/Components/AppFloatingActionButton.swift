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

struct AppFloatingActionButton: View {
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
                .padding(20)
                .background(buttonColors.containerColor)
                .accentColor(buttonColors.contentColor)
                .cornerRadius(50)
        }
    }
}
