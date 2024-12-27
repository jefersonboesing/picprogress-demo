//
//  CameraViewTest.swift
//  PicProgress
//
//  Created by Jeferson on 22/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import UIKit
import AVFoundation
import shared

public struct Camera: View {
    @ObservedObject var control: CameraControl
    
    public var body: some View {
        GeometryReader { geometry in
            VStack {
                PreviewHolder(captureSession: control.captureSession)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(.custom(.NeutralLowDarkest()))
            }
            .frame(maxWidth: geometry.size.width, maxHeight: geometry.size.height)
            .clipped()
        }
        .aspectRatio(3 / 4, contentMode: .fit)
        .clipped()
        .onAppear {
            control.requestAccess()
        }
    }
}

private class CameraPreviewView: UIView {
    
    private var captureSession: AVCaptureSession?
    
    var videoPreviewLayer: AVCaptureVideoPreviewLayer {
        return layer as! AVCaptureVideoPreviewLayer
    }
    
    init(captureSession: AVCaptureSession) {
        self.captureSession = captureSession
        super.init(frame: .zero)
    }
    
    override class var layerClass: AnyClass {
        AVCaptureVideoPreviewLayer.self
    }
    
    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        if let _ = self.superview, let session = self.captureSession {
            self.videoPreviewLayer.session = session
            self.videoPreviewLayer.videoGravity = .resizeAspectFill
        }
    }
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) not implemented")
    }
}

private struct PreviewHolder: UIViewRepresentable {
    var captureSession: AVCaptureSession
    
    func makeUIView(context: UIViewRepresentableContext<PreviewHolder>) -> CameraPreviewView {
        CameraPreviewView(captureSession: captureSession)
    }
    
    func updateUIView(_ uiView: CameraPreviewView, context: UIViewRepresentableContext<PreviewHolder>) {}
    
    typealias UIViewType = CameraPreviewView
}

public class CameraControl: NSObject, ObservableObject, AVCapturePhotoCaptureDelegate {
    
    @Published
    var image: UIImage? = nil
    
    @Published
    var cameraPosition: AVCaptureDevice.Position = .back
    
    @Published
    var isAllowed: Bool = false
    
    @Published
    var captureSession: AVCaptureSession = AVCaptureSession()
    
    @Published
    var flashMode: AVCaptureDevice.FlashMode = .auto
    
    private var capturePhotoOutput: AVCapturePhotoOutput? = nil
    
    func takePhoto() {
        let settings = AVCapturePhotoSettings()
        settings.flashMode = flashMode
        capturePhotoOutput?.capturePhoto(with: settings, delegate: self)
    }
    
    func requestAccess() {
        AVCaptureDevice.requestAccess(for: .video) { (isAllowed) in
            DispatchQueue.main.async {
                self.isAllowed = isAllowed
                self.refreshSession()
            }
        }
    }

    func setCameraLens(cameraLens: CameraLens) {
        self.cameraPosition = cameraLens == .front ? .front : .back
        refreshSession()
    }
    
    func setFlashMode(flashMode: FlashMode) {
        switch flashMode {
        case .off:
            self.flashMode = .off
        case .on:
            self.flashMode = .on
        case .auto_:
            self.flashMode = .auto
        default:
            self.flashMode = .auto
        }
    }
    
    private func refreshSession() {
        releaseSession()
        self.captureSession.beginConfiguration()
        guard let videoDevice = self.getCamera(with: cameraPosition), let videoDeviceInput = try? AVCaptureDeviceInput(device: videoDevice), self.captureSession.canAddInput(videoDeviceInput) else {
            return
        }
        let videoCaptureOutput = AVCapturePhotoOutput()
        self.captureSession.addInput(videoDeviceInput)
        self.captureSession.addOutput(videoCaptureOutput)
        self.captureSession.commitConfiguration()
        self.captureSession.startRunning()
        self.capturePhotoOutput = videoCaptureOutput
    }
    
    func releaseSession() {
        self.captureSession.stopRunning()
        self.captureSession.inputs.forEach { self.captureSession.removeInput($0) }
        self.captureSession.outputs.forEach { self.captureSession.removeOutput($0) }
    }
    
    public func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        if let imageData = photo.fileDataRepresentation() {
            var takenImage = UIImage(data: imageData)
            if cameraPosition == .front {
                takenImage = UIImage(cgImage: takenImage!.cgImage!, scale: takenImage!.scale, orientation: .leftMirrored)
            }
            self.image = takenImage
        }
    }
    
    func getCamera(with position: AVCaptureDevice.Position) -> AVCaptureDevice? {
        let devices = AVCaptureDevice.DiscoverySession(deviceTypes: [.builtInWideAngleCamera], mediaType: .video, position: position).devices
        return devices.first
    }
    
}
