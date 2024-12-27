//
//  AppButtonType.swift
//  PicProgress
//
//  Created by Jeferson on 22/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

enum AppButtonType {
    case primary
    case secondary
}

extension AppButtonType {
    
    func getButtonColors() -> AppButtonColors {
        let colors: (container: AppColor, content: AppColor)
        switch self {
        case .primary:
            colors = (.PrimaryMedium(), .NeutralHighLightest())
        case .secondary:
            colors = (.PrimaryLightest(), .PrimaryDark())
        }
        return AppButtonColors(
            containerColor: colors.container.asUIColor(),
            contentColor: colors.content.asUIColor()
        )
    }
    
    func getToggleButtonColors(enabled: Bool, checked: Bool) -> AppButtonColors {
            let colors: (container: AppColor, content: AppColor)
            switch (self, enabled, checked) {
            case (.primary, false, _):
                colors = (.PrimaryLight(), .PrimaryLightest())
            case (.primary, true, true):
                colors = (.PrimaryMedium(), .NeutralHighLightest())
            case (.primary, true, false):
                colors = (.PrimaryLightest(), .PrimaryMedium())
            case (.secondary, false, _):
                colors = (.NeutralLowDark(), .NeutralLowMedium())
            case (.secondary, true, true):
                colors = (.NeutralHighLightest(), .NeutralLowDarkest())
            case (.secondary, true, false):
                colors = (.NeutralLowDark(), .NeutralHighLightest())
            }

            return AppButtonColors(
                containerColor: colors.container.asUIColor(),
                contentColor: colors.content.asUIColor()
            )
        }
}

struct AppButtonColors {
    let containerColor: Color
    let contentColor: Color
}

