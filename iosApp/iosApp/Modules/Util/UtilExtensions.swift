//
//  UtilExtensions.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension URL {
    
    func delete() {
        try? FileManager.default.removeItem(at: self)
    }
}


extension Array {
    subscript(safe index: Index) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}
