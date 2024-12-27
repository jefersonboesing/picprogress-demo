//
//  GalleryItemLoader.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class GalleryItemLoader: ObservableObject {
    @Published var image: Image? = nil
    
    func load(photoUrl: URL?, resizeFactor: CGFloat) {
        guard self.image == nil, photoUrl != nil else { return }
        Task {
            let resizeFactor = max(0, min(resizeFactor, 1))
            if let photoUrl = photoUrl,
                let data = try? Data(contentsOf: photoUrl),
                let uiImage = UIImage(data: data)?.resize(resizeFactor: resizeFactor) {
                DispatchQueue.main.async {
                    self.image = Image(uiImage: uiImage).resizable()
                }
            }
        }
    }
    
}
