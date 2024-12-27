//
//  Gallery.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct Gallery: View {
    var photos: [Photo]
    var selectedPhotos: [Photo]
    let onPhotoClick: (Photo) -> Void
    var showLabel: Bool = true

    var body: some View {
        GeometryReader { geometry in
            ScrollView {
                VStack {
                    LazyVGrid(columns: Array(repeating: .init(spacing: 5), count: 3), spacing: 5) {
                        ForEach(photos, id: \.uuid) { photo in
                            GalleryItem(photo: photo, selected: selectedPhotos.contains(where: { $0.uuid == photo.uuid }), onClick: onPhotoClick)
                        }
                    }
                    .padding(.top, 16)
                    Spacer()
                    if showLabel {
                        Text("\(photos.count) photos").font(.bodyMedium())
                    }
                }
                .frame(minHeight: geometry.size.height)
                .padding(.horizontal, 16)
            }
            .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding(.top, 1)
    }
}
