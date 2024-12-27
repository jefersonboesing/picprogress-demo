//
//  CalendarExt.swift
//  PicProgress
//
//  Created by Jeferson on 25/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

enum TextStyle {
    case full
    case fullStandalone
    case short
    case shortStandalone
    case narrow
    case narrowStandalone
}

extension Kotlinx_datetimeDayOfWeek {
    func name(style: TextStyle = .full) -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale.current
        let index = (Int(self.ordinal) + 1) % 7
        switch style {
        case .full, .fullStandalone:
            return formatter.weekdaySymbols[index]
        case .short, .shortStandalone:
            return formatter.shortWeekdaySymbols[index]
        case .narrow, .narrowStandalone:
            return formatter.veryShortWeekdaySymbols[index]
        }
    }
}

extension Kotlinx_datetimeMonth {
    func name(style: TextStyle = .full) -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale.current
        let index = Int(self.ordinal)
        switch style {
        case .full, .fullStandalone:
            return formatter.monthSymbols[index]
        case .short, .shortStandalone:
            return formatter.shortMonthSymbols[index]
        case .narrow, .narrowStandalone:
            return formatter.veryShortMonthSymbols[index]
        }
    }
}

extension Kotlinx_datetimeLocalDate {
    
    func format(pattern: String = "MMM dd, yyyy") -> String {
        let calendar = NSCalendar.current
        var dateComponents = DateComponents()
        dateComponents.year = Int(self.year)
        dateComponents.month = Int(self.monthNumber)
        dateComponents.day = Int(self.dayOfMonth)
        let date = calendar.date(from: dateComponents) ?? Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = pattern
        return dateFormatter.string(from: date).capitalized
    }
    
}

extension Date {
    func toKotlinxLocalDate() -> Kotlinx_datetimeLocalDate {
        let components = Calendar.current.dateComponents([.year, .month, .day], from: self)
        return Kotlinx_datetimeLocalDate(
            year: Int32(components.year!),
            monthNumber: Int32(components.month!),
            dayOfMonth: Int32(components.day!)
        )
    }
}

extension Kotlinx_datetimeLocalDate {
    func toSwiftDate() -> Date {
        var components = DateComponents()
        components.year = Int(year)
        components.month = Int(monthNumber)
        components.day = Int(dayOfMonth)

        let calendar = Calendar.current
        return calendar.date(from: components) ?? Date()
    }
}
