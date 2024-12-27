//
//  ToastModifier.swift
//  PicProgress
//
//  Created by Jeferson on 01/02/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppToastContent: View {
    var message: AppToastMessage

    var body: some View {
        HStack(alignment: .top, spacing: 16) {
            VStack(alignment: .leading, spacing: 4) {
                Text(message.title)
                    .font(.titleMedium())
                    .foregroundColor(.custom(.NeutralLowDarkest()))
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(message.description)
                    .font(.bodyMedium())
                    .foregroundColor(.custom(.NeutralLowMedium()))
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            .frame(maxWidth: .infinity)

            Image(message.type.icon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 24, height: 24)
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .background(.custom(.NeutralHighLightest()))
        .cornerRadius(12)
        .shadow(radius: 10)
        .padding(16)
    }
}

struct ToastModifier: ViewModifier {
    @Binding var message: AppToastMessage?
    var duration: Double = 3
    
    func body(content: Content) -> some View {
        ZStack {
            content.blur(radius: message != nil ? 1 : 0)

            if let message = message {
                VStack(alignment: .leading) {
                    AppToastContent(message: message)
                    Spacer()
                }
                .background(Color.clear.contentShape(Rectangle()))
                .onTapGesture {
                    withAnimation {
                        self.message = nil
                    }
                }
                .onAppear {
                    DispatchQueue.main.asyncAfter(deadline: .now() + duration) {
                        withAnimation {
                            self.message = nil
                        }
                    }
                }
            }
        }
    }
}

extension View {
    func appToast(message: Binding<AppToastMessage?>, duration: Double = 3) -> some View {
        self.modifier(ToastModifier(message: message, duration: duration))
    }
}

struct AppToastMessage {
    let title: LocalizedStringKey
    let description: LocalizedStringKey
    let type: ToastType
}

func createAppToastMessage(message: ToastMessage, type: ToastType) -> AppToastMessage {
    return AppToastMessage(title: message.localizedTitle, description: message.localizedDescription, type: type)
}

extension ToastMessage {
    var localizedTitle: LocalizedStringKey {
        switch self {
        case .deletealbumfailure:
            return "Error deleting the album"
        case .deletealbumphotosfailure:
            return "Error deleting the photos"
        case .deletephotofailure:
            return "Error deleting the photo"
        case .addphotofailure:
            return "Error saving the photo"
        default:
            return ""
        }
    }
    
    var localizedDescription: LocalizedStringKey {
        switch self {
        case .deletealbumfailure:
            return "Please try again."
        case .deletealbumphotosfailure:
            return "Please try again."
        case .deletephotofailure:
            return "Please try again."
        case .addphotofailure:
            return "Please try again."
        default:
            return ""
        }
    }
}

extension ToastType {
    var icon: ImageResource {
        switch self {
        case .success:
            return .icSuccess
        default:
            return .icError
        }
    }
}
