//
//  Loading.swift
//  PicProgress
//
//  Created by Jeferson on 26/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CircularProgressView: View {
    @State private var isAnimating = false

    var body: some View {
        Circle()
            .trim(from: 0.0, to: 0.7)
            .stroke(style: StrokeStyle(lineWidth: 8, lineCap: .round))
            .foregroundColor(.custom(.PrimaryMedium()))
            .rotationEffect(Angle(degrees: isAnimating ? 360 : 0))
            .frame(width: 64, height: 64)
            .onAppear {
                withAnimation(.linear(duration: 1).repeatForever(autoreverses: false)) {
                    self.isAnimating = true
                }
            }
    }
}
