//
//  BaseViewModelExtension.swift
//  PicProgress
//
//  Created by Jeferson on 02/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension BaseViewModel : ObservableObject {
    
}

extension AlbumWithSummary: Identifiable {
    
}

extension Photo {
    
    func getAbsolutePath() -> URL {
        if (FileManager.default.fileExists(atPath: photoPath.path)) {
            return URL(fileURLWithPath: photoPath.path)
        } else {
            let documentsDirectory = try? FileManager.default.url(
                for: .documentDirectory,
                in: .userDomainMask,
                appropriateFor: nil,
                create: false
            )
            return documentsDirectory?.appendingPathComponent(photoPath.path) ?? URL(fileURLWithPath: "")
        }
    }
    
}

extension PhotoPath {
    
    func getAbsolutePath() -> URL? {
        let documentsUrl = try? FileManager.default.url(
            for: .documentDirectory,
            in: .userDomainMask,
            appropriateFor: nil,
            create: false
        )
        return documentsUrl?.appendingPathComponent(path)
    }
    
}
