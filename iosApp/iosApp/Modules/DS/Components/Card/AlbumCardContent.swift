//
//  AlbumCardContent.swift
//  PicProgress
//
//  Created by Jeferson on 25/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumCardContent: View {
    
    let album: Album
    let summary: AlbumSummary
    let animate: Bool
    
    var body: some View {
        LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 4), count: album.getMaxItemsInEachRow()), spacing: 4) {
            
            switch album.frequency {
            case .daily:
                ForEach(summary.allFrames, id: \.self) { frame in
                    FloatingTimeFrame(
                        text: frame.start.dayOfWeek.name(style: .narrow),
                        isCurrent: frame.isCurrent(),
                        isCompleted: summary.completedFrames.contains(frame),
                        theme: album.theme,
                        animate: animate
                    )
                }
            case .monthly:
                ForEach(summary.allFrames, id: \.self) { (frame: TimeFrame) in
                    let isFutureMonth = frame.start.monthNumber > DateExtKt.today().monthNumber
                    CircularTimeFrame(
                        text: isFutureMonth ? "" : frame.start.month.name(style: .short),
                        isCompleted: summary.completedFrames.contains(frame),
                        colors: album.theme
                    )
                }
            default:
                ForEach(summary.allFrames, id: \.self) { frame in
                    let weekNumber = frame.start.getWeekNumber(referenceDate: album.createdAt.date)
                    FloatingTimeFrame(
                        text: String(format: NSLocalizedString("Week", comment: ""), weekNumber),
                        isCurrent: frame.isCurrent(),
                        isCompleted: summary.completedFrames.contains(frame),
                        theme: album.theme,
                        animate: animate
                    )
                }
            }
            
        }.id(UUID().uuidString)
    }
}

extension Album {
    func getMaxItemsInEachRow() -> Int {
        switch self.frequency {
        case .monthly:
            return 6
        case .weekly:
            return 4
        default:
            return 7
        }
    }
}
