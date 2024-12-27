//
//  Previews.swift
//  PicProgress
//
//  Created by Jeferson on 22/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

#Preview {
    VStack {
        AppButton("Compare", startIcon: .icCompare, action: {})
        AppButtonSmall("Cancel", action: {})
        AppFloatingActionButton(.icPlus, type: .secondary, action: {})
        AppIconButton(.icPlus, type: .secondary, action: {})
        AppFilterButton("All", icon: .icArrowUpDown, action: {})
        AppToggleButton(.icCombine, checked: true, enabled: true, action: {})
        AppToggleButton(.icCombine, checked: false, enabled: true, action: {})
        AppToggleButton(.icCombine, type: .secondary, checked: false, enabled: true, action: {})
        AppToggleButton(.icCombine, type: .secondary, checked: true, enabled: true, action: {})
    }.padding(16)
}
