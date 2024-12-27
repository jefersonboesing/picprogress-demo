
import Foundation
import SwiftUI

private let FONT_DM_SANS_REGULAR = "DMSans-Regular"
private let FONT_DM_SANS_MEDIUM = "DMSans-Medium"
private let FONT_DM_SANS_BOLD = "DMSans-Bold"

extension Font {
    static func headlineLarge() -> Font {
        return Font.custom(FONT_DM_SANS_BOLD, size: 32)
    }
    
    static func titleLarge() -> Font {
        return Font.custom(FONT_DM_SANS_MEDIUM, size: 20)
    }
    
    static func titleMedium() -> Font {
        return Font.custom(FONT_DM_SANS_MEDIUM, size: 16)
    }
    
    static func titleSmall() -> Font {
        return Font.custom(FONT_DM_SANS_MEDIUM, size: 14)
    }
    
    static func bodyLarge() -> Font {
        return Font.custom(FONT_DM_SANS_REGULAR, size: 16)
    }
    
    static func bodyMedium() -> Font {
        return Font.custom(FONT_DM_SANS_REGULAR, size: 14)
    }
    
    static func bodySmall() -> Font {
        return Font.custom(FONT_DM_SANS_REGULAR, size: 12)
    }
    
    static func labelLarge() -> Font {
        return Font.custom(FONT_DM_SANS_REGULAR, size: 16)
    }
    
    static func labelMedium() -> Font {
        return Font.custom(FONT_DM_SANS_MEDIUM, size: 12)
    }
    
    static func labelSmall() -> Font {
        return Font.custom(FONT_DM_SANS_REGULAR, size: 10)
    }
    
    static func labelShare() -> Font {
        return Font.custom(FONT_DM_SANS_MEDIUM, size: 10)
    }
    
    static func onboardingTitle() -> Font {
        return Font.custom(FONT_DM_SANS_BOLD, size: 28)
    }
}
