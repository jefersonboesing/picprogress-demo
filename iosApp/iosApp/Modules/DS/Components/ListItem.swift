//
//  ListItem.swift
//  PicProgress
//
//  Created by Jeferson on 24/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ListItem: View {
    var label: LocalizedStringKey
    var startContent: (() -> AnyView)? = nil
    var endContent: (() -> AnyView)? = nil
    var endIcon: ImageResource? = nil
    var selected: Bool = false
    var onClick: (() -> Void)? = nil

    var body: some View {
        Button(action: { onClick?()}) {
            HStack(spacing: 12) {
                if let startContent = startContent {
                    startContent()
                }
                Text(label)
                    .frame(minHeight: 24)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .font(.bodyLarge())
                if let endContent = endContent {
                    endContent()
                }
                if let endIcon = endIcon {
                    Image(endIcon)
                }
                if selected {
                    Image(.icCheck)
                }
            }
            .padding(.vertical, 12)
            .background(.white)
        }.buttonStyle(.plain)
    }
}


#Preview {
    ListItem(
        label: "Background color",
        startContent: {
            AnyView(
                Circle().fill(.red).frame(width: 50, height: 50)
            )
        },
        endContent: {
            AnyView(
                Circle().fill(.blue).frame(width: 50, height: 50)
            )
        },
        endIcon: .icArrowRight
    )
}
