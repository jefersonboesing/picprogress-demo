//
//  UIImageExtension.swift
//  PicProgress
//
//  Created by Jeferson on 02/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import PhotosUI
import shared

extension PhotosPickerItem {
    
    func loadData() async -> Data? {
        return await withCheckedContinuation { continuation in
            self.loadTransferable(type: Data.self) { result in
                switch result {
                case .success(let data):
                    continuation.resume(returning: data)
                case .failure(_):
                    continuation.resume(returning: nil)
                }
            }
        }
    }

    func saveToTemporaryFile() async -> URL? {
        guard let originalData = await loadData(), let image = UIImage(data: originalData)
        else { return nil
        }
        let originalSizeKB = Double(originalData.count) / 1024.0
        print("Original image size: \(originalSizeKB) KB")
        let resizedImage = image.resize(resizeFactor: 0.2)
        let exifData = ExifData(data: originalData)
        return await image.saveToTemporaryFileJPEG(creationDate: exifData.dateTimeOriginal ?? exifData.dateTimeDigitized ?? Date())
    }
}
