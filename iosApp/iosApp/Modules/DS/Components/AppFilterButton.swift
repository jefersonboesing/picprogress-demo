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

struct AppFilterButton: View {
    let text: LocalizedStringKey
    let icon: ImageResource
    let action: () -> Void

    init(
        _ text: LocalizedStringKey,
        icon: ImageResource,
        action: @escaping () -> Void
    ) {
        self.text = text
        self.icon = icon
        self.action = action
    }
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 4) {
                Text(text).font(.labelMedium())
                Image(icon).renderingMode(.template).resizable().frame(width: 12, height: 12)
            }
            .accentColor(.custom(.NeutralLowMedium()))
            .padding(8)
            .background(.custom(.NeutralHighLight()))
            .cornerRadius(8)
        }
    }
}
