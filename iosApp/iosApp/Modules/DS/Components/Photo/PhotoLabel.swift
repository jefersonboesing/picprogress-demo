//
//  PhotoLabel.swift
//  PicProgress
//
//  Created by Jeferson on 30/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoLabel: View {
    var title: LocalizedStringKey
    var photo: Photo?

    var body: some View {
        VStack(spacing: 4) {
            Text(title)
                .font(.titleMedium())
                .foregroundStyle(.custom(.NeutralLowDarkest()))
            Text(photo?.createdAt.date.format() ?? "")
                .font(.bodySmall())
                .foregroundStyle(.custom(.NeutralLowMedium()))
        }
        .frame(maxWidth: .infinity)
    }
}
