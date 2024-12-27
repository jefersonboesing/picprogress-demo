//
//  GalleryItem.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct GalleryItem: View {
    @StateObject private var imageLoader = GalleryItemLoader()
    
    let photo: Photo
    let selected: Bool
    let onClick: (Photo) -> Void

    var body: some View {
        Button(action: { onClick(photo) }) {
            GeometryReader { geometry in
                imageLoader.image?
                    .imageScale(.small)
                    .aspectRatio(contentMode: .fill)
                .frame(width: geometry.size.width, height: geometry.size.height)
                .clipShape(RoundedRectangle(cornerRadius: 8))
                .overlay {
                    if selected {
                        ZStack(alignment: .bottomTrailing) {
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(.custom(.PrimaryMedium()), lineWidth: 4)
                                .background(.white.opacity(0.4))
                                .tint(.white.opacity(0.4))
                            Image(.icCheck)
                                .resizable()
                                .frame(width: 24, height: 24, alignment: .bottom)
                                .foregroundColor(.white)
                                .padding(8)
                        }
                    }
                }
            }
            .aspectRatio(1, contentMode: .fit)
        }
        .onAppear {
            imageLoader.load(photoUrl: photo.getAbsolutePath(), resizeFactor: 0.3)
        }
        .background(Color.black.opacity(0.1))
        .cornerRadius(8)
    }
}
