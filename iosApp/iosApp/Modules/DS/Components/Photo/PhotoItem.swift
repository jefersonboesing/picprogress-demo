//
//  PhotoItem.swift
//  PicProgress
//
//  Created by Jeferson on 15/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PhotoItem: View {
    @ObservedObject private var viewModel: PhotoItemViewModel
    @State private var photoImage: UIImage?
    private let onLeftTap: (() -> Void)?
    private let onRightTap: (() -> Void)?
    private let onPress: (() -> Void)?
    private let onRelease: (() -> Void)?
    private let shape: RoundedRectangle
    
    init(photo: Photo, onLeftTap: ( () -> Void)? = nil, onRightTap: ( () -> Void)? = nil, onPress: ( () -> Void)? = nil, onRelease: ( () -> Void)? = nil, shape: RoundedRectangle = RoundedRectangle(cornerRadius: 0)) {
        self.viewModel = PhotoItemViewModel(photo: photo)
        self.onLeftTap = onLeftTap
        self.onRightTap = onRightTap
        self.onPress = onPress
        self.onRelease = onRelease
        self.shape = shape
    }
    
    var body: some View {
        
        GeometryReader { geometry in
            
            if let image = photoImage {
                Image(uiImage: image).resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: geometry.size.width)
                    .scaledToFill()
                    .frame(maxWidth: geometry.size.width, maxHeight: geometry.size.height)
                    .clipped()
                    .clipShape(shape)
                    .overlay {
                        gestureOverlay(geometry: geometry)
                    }
                
            } else {
                VStack {
                    CircularProgressView()
                }
                .frame(maxWidth: geometry.size.width, maxHeight: geometry.size.height)
                .overlay {
                    gestureOverlay(geometry: geometry)
                }
            }
        }
        .aspectRatio(3 / 4, contentMode: .fit)
        .clipped()
        .onAppear { ImageCache.shared.image(for: viewModel.photo.getAbsolutePath()) { self.photoImage = $0 } }
        .onChange(of: viewModel.photo) { newURL in
            ImageCache.shared.image(for: newURL.getAbsolutePath()) { self.photoImage = $0 }
        }
        
    }
    
    @ViewBuilder
    private func gestureOverlay(geometry: GeometryProxy) -> some View {
        if onLeftTap != nil || onRightTap != nil || onPress != nil || onRelease != nil {
            Color.clear.contentShape(Rectangle()).gesture(
                DragGesture(minimumDistance: 0)
                    .onChanged { _ in onPress?() }
                    .onEnded { value in
                        if value.startLocation.x < geometry.size.width / 2 {
                            onLeftTap?()
                        } else {
                            onRightTap?()
                        }
                        onRelease?()
                    }
            )
        }
    }
}

private class PhotoItemViewModel: ObservableObject {
    @Published var photo: Photo
    
    init(photo: Photo) {
        self.photo = photo
    }
}
