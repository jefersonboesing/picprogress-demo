//
//  AppView.swift
//  PicProgress
//
//  Created by Jeferson on 19/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct AppView: View {
    
    @EnvironmentObject
    private var appNavigation: AppNavigation

    var body: some View {
        NavigationStack(path: $appNavigation.navigationPath) {
            HomeView(navigateTo: appNavigation.navigateTo, goBack: appNavigation.goBack)
                .navigationDestination(for: AppNavigation.Destination.Page.self) { destination in
                    switch destination {
                    case .home:
                        HomeView(navigateTo: appNavigation.navigateTo, goBack: appNavigation.goBack)
                    case .album(let album):
                        AlbumView(album: album, navigateTo: appNavigation.navigateTo, goBack: appNavigation.goBack)
                    case .photo(let album, let photo):
                        PhotoView(album: album, photo: photo, navigateTo: appNavigation.navigateTo, goBack: appNavigation.goBack)
                    case .compare(let album, let comparePhotos):
                        CompareView(album: album, comparePhotos: comparePhotos, navigateTo: appNavigation.navigateTo, goBack: appNavigation.goBack)
                    case .camera(let album):
                        CameraView(album: album, navigateTo: appNavigation.navigateTo, goBack:appNavigation.goBack)
                    case .preview(let album, let photoPath):
                        PreviewView(photoPath: photoPath, album: album, goBack: appNavigation.goBack, popTo: appNavigation.popTo)
                    default:
                        EmptyView()
                    }
                }
        }
        .sheet(item: $appNavigation.activeSheet) { item in
            switch item {
            case .albumConfig(let album):
                AlbumConfigSheet(album: album, goBack: appNavigation.goBack).environmentObject(AlbumConfigNavigation())
                    .presentationDetents([.large])
                    .presentationCornerRadius(24)
            case .photoSelection(let album, let initialSelection, let unavailablePhotos, let minRequired):
                PhotoSelectionSheet(album: album, initialSelection: initialSelection, unavailablePhotos: unavailablePhotos, minRequired: minRequired, goBack: appNavigation.goBack, goBackWithResult: appNavigation.goBackWithResult(_:))
                    .presentationDetents([.large])
                    .presentationCornerRadius(24)
            case .photoConfig(photo: let photo):
                PhotoConfigSheet(photo: photo, goBack: appNavigation.goBack)
                    .presentationDetents([.large])
                    .presentationCornerRadius(24)
            }
        }
    }
}
