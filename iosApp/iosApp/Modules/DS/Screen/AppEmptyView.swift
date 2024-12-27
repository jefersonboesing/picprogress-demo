//
//  AppEmptyView.swift
//  PicProgress
//
//  Created by Jeferson on 01/02/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AppEmptyView: View {
    let image: ImageResource
    let title: LocalizedStringKey
    let description: LocalizedStringKey

    var body: some View {
        VStack(spacing: 0) {
            Image(image)
                .aspectRatio(contentMode: .fit)
            Text(title)
                .font(.titleLarge())
                .foregroundColor(.custom(.PrimaryDarkest()))
                .padding(.horizontal, 55)
                .multilineTextAlignment(.center)
            Text(description)
                .font(.bodyLarge())
                .foregroundColor(.custom(.PrimaryLight()))
                .padding(.top, 20)
                .padding(.horizontal, 55)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(.custom(.NeutralHighLightest()))
        .edgesIgnoringSafeArea(.all)
    }
}
