//
//  AppDialog.swift
//  PicProgress
//
//  Created by Jeferson on 22/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppDialog: View {
    
    let icon: ImageResource
    let title: LocalizedStringKey
    let message: LocalizedStringKey
    let positiveButtonText: LocalizedStringKey
    let negativeButtonText: LocalizedStringKey
    let onPositiveButtonClick: () -> Void
    let onNegativeButtonClick: () -> Void
    
    var body: some View {
        VStack(spacing: 0) {
            Image(icon)
                .renderingMode(.template)
                .resizable()
                .frame(width: 40, height: 40)
                .padding(24)
                .foregroundColor(.custom(.PrimaryMedium()))
                .background(.custom(.PrimaryLightest()))
                .cornerRadius(100)
            
            Text(title)
                .multilineTextAlignment(.center)
                .foregroundColor(.custom(.NeutralLowDarkest()))
                .font(.titleLarge())
                .padding(.top, 40)
            
            Text(message)
                .multilineTextAlignment(.center)
                .foregroundColor(.custom(.NeutralLowMedium()))
                .font(.bodyLarge())
                .padding(.top, 16)
                .padding(.horizontal, 16)
            
            HStack(spacing: 8) {
                AppButton(negativeButtonText, type: .secondary, action: onNegativeButtonClick)
                AppButton(positiveButtonText, type: .primary, action: onPositiveButtonClick)
            }
            .padding(.top, 40)
            
        }
        .padding(.horizontal, 24)
        .padding(.top, 40)
        .padding(.bottom, 24)
        .frame(maxWidth: .infinity)
        .background(.custom(.NeutralHighLightest()))
        .cornerRadius(15)
        .padding(16)
        
    }
}

#Preview {
    AppDialog(
        icon: .icTrashDialog,
        title: "Are you sure you want to delete this photo?",
        message: "After deleting, it will no longer be possible to access them.",
        positiveButtonText: "Delete",
        negativeButtonText: "Cancel",
        onPositiveButtonClick: {},
        onNegativeButtonClick: {}
    )
}

struct AppDialogModifier: ViewModifier {
    let icon: ImageResource
    let title: LocalizedStringKey
    let message: LocalizedStringKey
    let positiveButtonText: LocalizedStringKey
    let negativeButtonText: LocalizedStringKey
    let onPositiveButtonClick: () -> Void
    let onNegativeButtonClick: () -> Void
    let isVisible: Bool
    
    init(icon: ImageResource, title: LocalizedStringKey, message: LocalizedStringKey, positiveButtonText: LocalizedStringKey, negativeButtonText: LocalizedStringKey, onPositiveButtonClick: @escaping () -> Void, onNegativeButtonClick: @escaping () -> Void, isVisible: Bool) {
        self.icon = icon
        self.title = title
        self.message = message
        self.positiveButtonText = positiveButtonText
        self.negativeButtonText = negativeButtonText
        self.onPositiveButtonClick = onPositiveButtonClick
        self.onNegativeButtonClick = onNegativeButtonClick
        self.isVisible = isVisible
    }
    
    func body(content: Content) -> some View {
        ZStack {
            content.blur(radius: isVisible ? 1 : 0)
            
            if isVisible {
                AppDialog(
                    icon: icon,
                    title: title,
                    message: message,
                    positiveButtonText: positiveButtonText,
                    negativeButtonText: negativeButtonText,
                    onPositiveButtonClick: onPositiveButtonClick,
                    onNegativeButtonClick: onNegativeButtonClick
                ).offset(y: -50).shadow(radius: 20)
            }
        }
    }
}

extension View {
    
    func appDialog(icon: ImageResource, title: LocalizedStringKey, message: LocalizedStringKey, positiveButtonText: LocalizedStringKey, negativeButtonText: LocalizedStringKey, onPositiveButtonClick: @escaping () -> Void, onNegativeButtonClick: @escaping () -> Void, isVisible: Bool) -> some View {
        return modifier(
            AppDialogModifier(
                icon: icon,
                title: title,
                message: message,
                positiveButtonText: positiveButtonText,
                negativeButtonText: negativeButtonText,
                onPositiveButtonClick: onPositiveButtonClick,
                onNegativeButtonClick: onNegativeButtonClick,
                isVisible: isVisible
            )
        )
    }
    
}
