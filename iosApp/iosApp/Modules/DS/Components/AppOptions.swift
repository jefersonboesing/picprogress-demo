//
//  AppOptions.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct AppOptionsModifier: ViewModifier {
    
    @ObservedObject
    private var state: AppOptionsState
    
    let title: LocalizedStringKey?
    let options: [AppOption]
    let onDismiss: () -> Void
    
    init(isPresented: Bool, title: LocalizedStringKey? = nil, options: [AppOption], onDismiss: @escaping () -> Void) {
        self.state = AppOptionsState(isPresented: isPresented)
        self.options = options
        self.onDismiss = onDismiss
        self.title = title
    }
    
    func body(content: Content) -> some View {
        content.sheet(
            isPresented: $state.isPresented,
            onDismiss: {
                if let optionSelected = state.optionSelected {
                    optionSelected.onClick()
                } else {
                    onDismiss()
                }
            }
        ) {
            let titleHeight = (title != nil) ? 60.0 : 0.0
            let itemHeight = 48.0
            let contentPadding = 60.0
            let height: CGFloat = titleHeight + contentPadding + CGFloat(options.count) * itemHeight
            VStack(spacing: 0) {
                Rectangle().foregroundColor(.clear).frame(width: 56, height: 6).background(Color(red: 0.8, green: 0.8, blue: 0.8)).cornerRadius(3).padding(.top, 16).padding(.bottom, 20)
                if let title {
                    Text(title).font(.titleLarge())
                        .padding(.top, 12)
                        .padding(.bottom, 20)
                }
                ForEach(options) { option in
                    ListItem(
                        label: option.text,
                        endIcon: option.icon,
                        selected: option.selected
                    ) {
                        DispatchQueue.main.async {
                            state.optionSelected = option
                            state.isPresented = false
                        }
                    }
                }
            }
            .presentationDetents([.height(height)])
            .presentationCornerRadius(24)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .padding(.horizontal, 16)
        }
    }
}

private class AppOptionsState: ObservableObject {
    @Published var isPresented: Bool
    @Published var optionSelected: AppOption? = nil
    
    init(isPresented: Bool) {
        self.isPresented = isPresented
    }
}

struct AppOption: Identifiable {
    let id = UUID()
    let text: LocalizedStringKey
    var icon: ImageResource? = nil
    var selected: Bool = false
    let onClick: () -> Void
}

extension View {
    
    func appOptionsSheet(
        isPresented: Bool,
        title: LocalizedStringKey? = nil,
        options: [AppOption],
        onDismiss: @escaping () -> Void
    ) -> some View {
        return modifier(
            AppOptionsModifier(isPresented: isPresented, title: title, options: options, onDismiss: onDismiss)
        )
    }
    
}
