//
//  UIImageExtension.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension UIImage {

    func resize(resizeFactor: CGFloat) -> UIImage {
        if resizeFactor >= 1 {
            return self
        }
        let size = CGSize(width: self.size.width * resizeFactor, height: self.size.height * resizeFactor)
        let format = UIGraphicsImageRendererFormat()
        format.scale = 1

        return UIGraphicsImageRenderer(size: size, format: format).image { _ in
            draw(in: CGRect(origin: .zero, size: size))
        }
    }

    func saveToTemporaryFileJPEG(creationDate: Date = Date(), fileName : String = UUID().uuidString) async -> URL? {
        guard let data = jpegData(compressionQuality: 0.3) else { return nil }
        
        let fileUrl = FileManager.default.temporaryDirectory.appendingPathComponent(fileName).appendingPathExtension("jpeg")
        do {
            let newSizeKB = Double(data.count) / 1024.0
            print("New image size: \(newSizeKB) KB")
            try data.write(to: fileUrl)
            try FileManager.default.setAttributes([.creationDate: creationDate], ofItemAtPath: fileUrl.path)
            return fileUrl
        } catch {
            return nil
        }
    }

    func saveToTemporaryFilePNG(creationDate: Date = Date(), fileName : String = UUID().uuidString) async -> URL? {
            guard let data = pngData() else { return nil }

            let fileUrl = FileManager.default.temporaryDirectory.appendingPathComponent(fileName).appendingPathExtension("png")
            do {
                try data.write(to: fileUrl)
                try FileManager.default.setAttributes([.creationDate: creationDate], ofItemAtPath: fileUrl.path)
                return fileUrl
            } catch {
                return nil
            }
        }
}
