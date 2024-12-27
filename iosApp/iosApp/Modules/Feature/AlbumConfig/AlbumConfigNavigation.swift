//
//  AlbumConfigNavigation.swift
//  PicProgress
//
//  Created by Jeferson on 02/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AlbumConfigNavigation: ObservableObject {
    @Published 
    var navigationPath: [Destination.Page] = []
    
    enum Destination: Hashable {
        enum Page: Hashable {
            case theme(current: AlbumTheme)
            case frequency(current: Frequency)
        }
            
        case page(Page)
    }

    func navigateTo(destination: Destination) {
        switch destination {
        case .page(let page):
            navigationPath.append(page)
        }
    }

    func goBack() {
        if (navigationPath.count > 0) {
            _ = navigationPath.popLast()
        }
    }
    
}
