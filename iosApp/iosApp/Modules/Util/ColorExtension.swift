import Foundation
import SwiftUI
import shared

extension AlbumTheme {
    func asSecondaryColor() -> Color {
        return secondaryColor.asColor()
    }
    
    func asSecondaryTextColor() -> Color {
        return secondaryTextColor.asColor()
    }
    
    func asPrimaryColor() -> Color {
        return primaryColor.asColor()
    }
    
    func asPrimaryTextColor() -> Color {
        return primaryTextColor.asColor()
    }
}

extension Int64 {
    func asColor() -> Color {
        let a = CGFloat((self & 0xFF000000) >> 24) / 255.0
        let r = CGFloat((self & 0x00FF0000) >> 16) / 255.0
        let g = CGFloat((self & 0x0000FF00) >> 8) / 255.0
        let b = CGFloat(self & 0x000000FF) / 255.0
        return Color(red: r, green: g, blue: b, opacity: a)
    }
}

extension AppColor {
    func asUIColor() -> Color {
        return color.asColor()
    }
}

extension Color {
    static func custom(_ appColor: AppColor) -> Color? {
        return appColor.asUIColor()
    }
}

extension ShapeStyle where Self == Color {
    static func custom(_ appColor: AppColor) -> Color {
        return appColor.asUIColor()
    }
}
