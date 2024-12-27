//
//  AppTextField.swift
//  PicProgress
//
//  Created by Jeferson on 22/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppTextField: View {
    @FocusState private var textFieldFocused: Bool
    @ObservedObject private var value: AppTextFieldValue
    let label: LocalizedStringKey
    let onValueChange: (String) -> Void
    var errorText: LocalizedStringKey?
    var placeholderText: LocalizedStringKey?
    var singleLine: Bool
    var maxLines: Int
    var minLines: Int
    
    init(label: LocalizedStringKey, value: String, onValueChange: @escaping (String) -> Void, errorText: LocalizedStringKey? = nil, placeholderText: LocalizedStringKey? = nil, singleLine: Bool = true, maxLines: Int = 1, minLines: Int = 1) {
        self.value = AppTextFieldValue(text: value)
        self.label = label
        self.onValueChange = onValueChange
        self.errorText = errorText
        self.placeholderText = placeholderText
        self.singleLine = singleLine
        self.maxLines = maxLines
        self.minLines = minLines
    }
    
    var body: some View {
        let strokeColor: Color = errorText != nil ? .custom(.StatusCriticalMedium()) : .clear
        let textFieldAxis: Axis = singleLine ? .horizontal : .vertical
        let keyboardSubmitLabel: SubmitLabel = singleLine ? .done : .return
        VStack(spacing: 0) {
            Text(label)
                .font(.bodyMedium())
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.bottom, 8)
            
            TextField(placeholderText ?? "", text: $value.text, axis: textFieldAxis)
                .font(.bodyMedium())
                .multilineTextAlignment(.leading)
                .lineLimit(minLines...maxLines)
                .onChange(of: value.text) { newValue in
                    onValueChange(newValue)
                }
                .padding(.vertical, 14)
                .padding(.horizontal, 16)
                .background(.custom(.NeutralHighLight()))
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12).stroke(strokeColor, lineWidth: 1)
                )
                .padding(.bottom, 4)
                .focused($textFieldFocused)
                .submitLabel(keyboardSubmitLabel)
                .onSubmit { textFieldFocused = false }
            if let errorText = errorText {
                Text(errorText)
                    .foregroundColor(.red)
                    .font(.bodySmall())
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .onTapGesture {
          textFieldFocused = true
         }
        .frame(maxWidth: .infinity)
    }
}

private class AppTextFieldValue: ObservableObject {
    @Published var text: String

    init(text: String) {
        self.text = text
    }
}


#if canImport(UIKit)
extension View {
    func endEditing() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
    
    func dismissKeyboardOnTap() -> some View {
        self.onTapGesture {
            endEditing()
        }
    }
}
#endif
