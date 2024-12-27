//
//  PhotoDate.swift
//  PicProgress
//
//  Created by Jeferson on 30/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoDate: View {
    var album: Album
    var photo: Photo

    var body: some View {
        VStack(spacing: 4) {
            Text(getLabelText())
                .font(.titleSmall())
                .frame(maxWidth: .infinity)
            Text(photo.createdAt.date.format())
                .font(.bodySmall())
                .frame(maxWidth: .infinity)
        }
        .padding(16)
    }

    func getLabelText() -> String {
        switch album.frequency {
        case .daily:
            return String(format: NSLocalizedString("Day", comment: ""), photo.createdAt.date.getDayNumber(referenceDate: album.createdAt.date))
        case .weekly:
            return String(format: NSLocalizedString("Week", comment: ""), photo.createdAt.date.getWeekNumber(referenceDate: album.createdAt.date))
        case .monthly:
            return String(format: NSLocalizedString("Month", comment: ""), photo.createdAt.date.getMonthNumber(referenceDate: album.createdAt.date))
        default:
            return ""
        }
    }
}
