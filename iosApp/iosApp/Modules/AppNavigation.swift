//
//  AppViewModel.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation


import SwiftUI
import shared

class AppNavigation: ObservableObject {
    @Published
    var navigationPath = NavigationPath()

    @Published
    var activeSheet: Destination.Sheet? = nil
    
    @Published
    var navigationResult: DestinationResult? = nil
    
    enum Destination: Hashable {
        enum Page: Hashable {
            case home
            case album(album: Album)
            case photo(album: Album, photo: Photo)
            case progress
            case compare(album: Album, comparePhotos: ComparePhotos? = nil)
            case camera(album: Album)
            case preview(album: Album, photoPath: PhotoPath)
        }
        
        enum Sheet: Hashable, Identifiable {
            var id: Self { self }
            case albumConfig(album: Album? = nil)
            case photoConfig(photo: Photo)
            case photoSelection(album: Album, initialSelection: [Photo], unavailablePhotos: [Photo], minRequired: Int32)
        }
        
        case page(Page)
        case sheet(Sheet)
    }
    
    enum DestinationResult: Hashable {
        case photoSelection(selectedPhotos: [Photo])
    }

    func navigateTo(destination: Destination) {
        switch destination {
        case .page(let page):
            navigationPath.append(page)
        case .sheet(let sheet):
            activeSheet = sheet
        }
    }

    func goBack() {
        if (activeSheet != nil) {
            activeSheet = nil
        } else if (navigationPath.count > 0) {
            navigationPath.removeLast()
        }
    }
    
    func goBackWithResult(_ result: DestinationResult) {
        self.navigationResult = result
        goBack()
    }
    
    func clearNavigationResult() {
        self.navigationResult = nil
    }
        
    func popTo(count: Int) {
        DispatchQueue.main.async { [self] in
            UIView.setAnimationsEnabled(false)
            navigationPath.removeLast(count)
            DispatchQueue.main.async {
                UIView.setAnimationsEnabled(true)
            }
        }
    }
}
