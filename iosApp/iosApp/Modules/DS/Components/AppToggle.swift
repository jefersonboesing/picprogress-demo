//
//  AppToggle.swift
//  PicProgress
//
//  Created by Jeferson on 02/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AppToggle : View {
    
    private var value: Bool
    private var onValueChange: (Bool) -> Void
    
    init(value: Bool = false, onValueChange: @escaping (Bool) -> Void) {
        self.value = value
        self.onValueChange = onValueChange
    }
    
    var body: some View {
        let binding = Binding(
            get: { self.value },
            set: { newValue in self.onValueChange(newValue)}
        )
        Toggle(isOn: binding) {}.tint(.custom(.PrimaryMedium()))
    }
    
}
