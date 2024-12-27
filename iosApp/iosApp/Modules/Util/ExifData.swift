//
//  ExifData.swift
//  PicProgress
//
//  Created by Jeferson on 14/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import PhotosUI

struct ExifData {
    var colorModel: String?
    var pixelWidth: Double?
    var pixelHeight: Double?
    var dpiWidth: Int?
    var dpiHeight: Int?
    var depth: Int?
    var orientation: Int?
    var apertureValue: String?
    var brightnessValue: String?
    var dateTimeDigitized: Date?
    var dateTimeOriginal: Date?
    var offsetTime: String?
    var offsetTimeDigitized: String?
    var offsetTimeOriginal: String?
    var model: String?
    var software: String?
    var tileLength: Double?
    var tileWidth: Double?
    var xResolution: Double?
    var yResolution: Double?
    var altitude: String?
    var destBearing: String?
    var hPositioningError: String?
    var imgDirection: String?
    var latitude: String?
    var longitude: String?
    var speed: Double?

    init(data: Data) {
        self.init(data: data as CFData)
    }
    
    init(data: CFData) {
        let options = [kCGImageSourceShouldCache as String: kCFBooleanFalse]
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy:MM:dd HH:mm:ss"

        if let imgSrc = CGImageSourceCreateWithData(data, options as CFDictionary),
           let metadata = CGImageSourceCopyPropertiesAtIndex(imgSrc, 0, options as CFDictionary) as? NSDictionary {
            self.colorModel = metadata[kCGImagePropertyColorModel] as? String
            self.pixelWidth = metadata[kCGImagePropertyPixelWidth] as? Double
            self.pixelHeight = metadata[kCGImagePropertyPixelHeight] as? Double
            self.dpiWidth = metadata[kCGImagePropertyDPIWidth] as? Int
            self.dpiHeight = metadata[kCGImagePropertyDPIHeight] as? Int
            self.depth = metadata[kCGImagePropertyDepth] as? Int
            self.orientation = metadata[kCGImagePropertyOrientation] as? Int

            if let tiffData = metadata[kCGImagePropertyTIFFDictionary] as? NSDictionary {
                self.model = tiffData[kCGImagePropertyTIFFModel] as? String
                self.software = tiffData[kCGImagePropertyTIFFSoftware] as? String
                self.tileLength = tiffData[kCGImagePropertyTIFFTileLength] as? Double
                self.tileWidth = tiffData[kCGImagePropertyTIFFTileWidth] as? Double
                self.xResolution = tiffData[kCGImagePropertyTIFFXResolution] as? Double
                self.yResolution = tiffData[kCGImagePropertyTIFFYResolution] as? Double
            }

            if let exifData = metadata[kCGImagePropertyExifDictionary] as? NSDictionary {
                self.apertureValue = exifData[kCGImagePropertyExifApertureValue] as? String
                self.brightnessValue = exifData[kCGImagePropertyExifBrightnessValue] as? String
                if let dateTimeDigitizedString = exifData[kCGImagePropertyExifDateTimeDigitized] as? String {
                    self.dateTimeDigitized = dateFormatter.date(from: dateTimeDigitizedString)
                }
                if let dateTimeOriginalString = exifData[kCGImagePropertyExifDateTimeOriginal] as? String {
                    self.dateTimeOriginal = dateFormatter.date(from: dateTimeOriginalString)
                }
                self.offsetTime = exifData[kCGImagePropertyExifOffsetTime] as? String
                self.offsetTimeDigitized = exifData[kCGImagePropertyExifOffsetTimeDigitized] as? String
                self.offsetTimeOriginal = exifData[kCGImagePropertyExifOffsetTimeOriginal] as? String
            }

            if let gpsData = metadata[kCGImagePropertyGPSDictionary] as? NSDictionary {
                self.altitude = gpsData[kCGImagePropertyGPSAltitude] as? String
                self.destBearing = gpsData[kCGImagePropertyGPSDestBearing] as? String
                self.hPositioningError = gpsData[kCGImagePropertyGPSHPositioningError] as? String
                self.imgDirection = gpsData[kCGImagePropertyGPSImgDirection] as? String
                self.latitude = gpsData[kCGImagePropertyGPSLatitude] as? String
                self.longitude = gpsData[kCGImagePropertyGPSLongitude] as? String
                self.speed = gpsData[kCGImagePropertyGPSSpeed] as? Double
            }
        }
    }

    init(url: URL) {
        if let data = NSData(contentsOf: url) {
            self.init(data: data as CFData)
        } else {
            self.init(data: Data())
        }
    }

    init(image: UIImage) {
        if let data = image.cgImage?.dataProvider?.data {
            self.init(data: data as CFData)
        } else {
            self.init(data: Data())
        }
    }
}
