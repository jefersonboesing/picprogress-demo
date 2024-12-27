//
//  Loading.swift
//  PicProgress
//
//  Created by Jeferson on 26/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LoadingScreen: View {

    @Binding
    var state: ProgressState

    
    var body: some View {
        VStack {
            switch state {
            case .none:
                EmptyView()
            case .indeterminate:
                CircularProgressView()
            case .determinate(let current, let total, let message):
                VStack {
                    ZStack {
                        CircularProgressView()
                        Text("\((current * 100)/total)%")
                            .font(.bodyMedium())
                            .frame(width: .infinity, height: .infinity, alignment: .center)
                    }
                    Text(message)
                        .font(.bodyMedium())
                        .frame(width: .infinity, height: .infinity, alignment: .center)
                        .padding()
                }
            }
        }
        .navigationBarBackButtonHidden()
    }
}

enum ProgressState {
    case none
    case indeterminate
    case determinate(current: Int, total: Int, message: LocalizedStringKey)
}
