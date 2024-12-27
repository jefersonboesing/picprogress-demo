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

struct AppButtonSmall: View {
    let text: LocalizedStringKey
    let type: AppButtonType
    let action: () -> Void

    init(
        _ text: LocalizedStringKey,
        type: AppButtonType = .secondary,
        action: @escaping () -> Void
    ) {
        self.text = text
        self.type = type
        self.action = action
    }
    
    var body: some View {
        let buttonColors = type.getButtonColors()
        Button(action: action) {
            Text(text).font(.titleSmall())
            .frame(height: 32)
            .accentColor(buttonColors.contentColor)
            .padding(.horizontal, 16)
            .padding(.vertical, 4)
            .background(buttonColors.containerColor)
            .cornerRadius(24)
        }
    }
}
