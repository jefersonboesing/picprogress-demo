//
//  AlbumCardContentSmall.swift
//  PicProgress
//
//  Created by Jeferson on 01/02/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumCardContentSmall: View {
    
    let album: Album
    let summary: AlbumSummary
    
    var body: some View {
        
        let currentFrame = summary.allFrames.first(where: { $0.isCurrent() })
        let frameText: String? = currentFrame.flatMap { frame in
            switch album.frequency {
            case .daily:
                return frame.start.dayOfWeek.name(style: .full)
            case .monthly:
                return frame.start.month.name(style: .full)
            default:
                let weekNumber = frame.start.getWeekNumber(referenceDate: album.createdAt.date)
                return String(format: NSLocalizedString("Week", comment: ""), weekNumber)
            }
        }
        
        if let frameText = frameText, let currentFrame = currentFrame {
            FloatingTimeFrame(
                text: frameText,
                isCurrent: false,
                isCompleted: summary.completedFrames.contains(currentFrame),
                theme: album.theme,
                textAlignment: .topLeading,
                animate: false
            )
        } else {
            EmptyView()
        }
    }
}

