//
//  AlbumCard.swift
//  PicProgress
//
//  Created by Jeferson on 25/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumCard: View {
    let album: Album
    let isSmallMode: Bool
    let summary: AlbumSummary
    var animate: Bool = true
    let onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            VStack {
                AlbumCardHeader(
                    album: album,
                    summary: summary,
                    isSmallMode: isSmallMode
                )
                Spacer()
                if isSmallMode {
                    AlbumCardContentSmall(
                        album: album,
                        summary: summary
                    )
                } else {
                    AlbumCardContent(
                        album: album,
                        summary: summary,
                        animate: animate
                    )
                }
            }
            .padding(16)
            .frame(maxWidth: .infinity, minHeight: 162, maxHeight: 162)
            
            .background(album.theme.asPrimaryColor())
            .cornerRadius(16)
        }.buttonStyle(.plain)
    }
}

struct AlbumCardHeader: View {
    let album: Album
    let summary: AlbumSummary
    let isSmallMode: Bool
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text(album.title.truncate(maxLength: 18))
                    .font(.titleLarge())
                    .foregroundColor(album.theme.asPrimaryTextColor())
                    .lineLimit(1)
                Spacer()
                if !isSmallMode {
                    Status(album: album, summary: summary)
                }
            }
            Text(summary.albumSize >= 0 ? "\(summary.albumSize) photo" : "")
                .font(.bodySmall())
                .foregroundColor(album.theme.asPrimaryTextColor())
        }
    }
}

private struct Status: View {
    let album: Album
    let summary: AlbumSummary
    
    var body: some View {
        if summary.isUpdated {
            HStack(spacing: 4) {
                Text("Updated")
                    .font(.bodySmall())
                    .foregroundColor(album.theme.asPrimaryTextColor())
                Image(.icCheckMini)
                    .renderingMode(.template)
                    .foregroundColor(album.theme.asPrimaryTextColor())
            }
        } else {
            Text(summary.timeRemaining?.toText() ?? "")
                .font(.bodySmall())
                .foregroundColor(album.theme.asPrimaryTextColor())
        }
    }
}

extension TimeRemaining {
    func toText() -> LocalizedStringKey {
        switch self {
        case let timeRemaining as TimeRemaining.Days:
            return "\(timeRemaining.days) days left"
        case let timeRemaining as TimeRemaining.Hours:
            return "\(timeRemaining.hours) hours left"
        case let timeRemaining as TimeRemaining.Minutes:
            return "\(timeRemaining.minutes) minutes left"
        default:
            return ""
        }
    }
}
