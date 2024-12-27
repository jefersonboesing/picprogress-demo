//
//  TutorialScreen.swift
//  PicProgress
//
//  Created by Jeferson on 14/06/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct TutorialScreen: View {
    let icon: ImageResource
    let message: LocalizedStringKey

    var body: some View {
        ZStack {
            Color.custom(.AdditionalTutorial()).ignoresSafeArea()
            GeometryReader { geo in
                
                VStack(spacing: 0) {
                    Spacer().frame(height: geo.size.height * 0.4)
                    Image(icon)
                    Text(message)
                        .font(.bodyMedium())
                        .foregroundColor(.custom(.NeutralHighLightest()))
                        .padding(.top, 8)
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
        }
        .allowsHitTesting(false)
    }
}

#Preview {
    TutorialScreen(
        icon: .icTutorialHold, message: "Hold to compare before and after"
    )
}

