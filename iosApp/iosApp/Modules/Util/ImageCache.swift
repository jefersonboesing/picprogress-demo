//
//  ImageCache.swift
//  PicProgress
//
//  Created by Jeferson on 11/06/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

class ImageCache {
    static let shared = ImageCache()

    private let cache = NSCache<NSString, UIImage>()
    private let queue = DispatchQueue(label: "com.picprogress.cache")

    func image(for url: URL, completion: @escaping (UIImage?) -> Void) {
        let key = url.absoluteString as NSString

        if let image = cache.object(forKey: key) {
            completion(image)
            return
        }

        queue.async {
            guard let data = try? Data(contentsOf: url),
                  let image = UIImage(data: data) else {
                DispatchQueue.main.async {
                    completion(nil)
                }
                return
            }

            self.cache.setObject(image, forKey: key)
            DispatchQueue.main.async {
                completion(image)
            }
        }
    }
}
