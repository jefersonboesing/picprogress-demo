//
//  AlbumConfigSheet.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AlbumConfigSheet: View {
    @EnvironmentObject
    private var albumConfigNavigation: AlbumConfigNavigation
    
    private let sharedViewModel = AlbumConfigViewModel()

    let album: Album?
    let goBack: () -> Void
    
    init(album: Album? = nil, goBack: @escaping () -> Void) {
        self.album = album
        self.goBack = goBack
    }
    
    var body: some View {
        NavigationStack(path: $albumConfigNavigation.navigationPath) {
            AlbumConfigMainView(album: album, navigateTo: albumConfigNavigation.navigateTo, goBack: goBack)
                .navigationDestination(for: AlbumConfigNavigation.Destination.Page.self) { destination in
                    switch destination {
                    case .theme(let current):
                        AlbumConfigThemeView(currentTheme: current, goBack: albumConfigNavigation.goBack)
                    case .frequency(let current):
                        AlbumConfigFrequencyView(currentFrequency: current, goBack: albumConfigNavigation.goBack)
                    }
                }
        }.environmentObject(sharedViewModel)
    }
}
