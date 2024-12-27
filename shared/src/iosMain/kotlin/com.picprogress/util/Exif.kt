//@file:OptIn(ExperimentalForeignApi::class)
//
//package com.picprogress.util
//
//import kotlinx.cinterop.ExperimentalForeignApi
//import platform.CoreFoundation.CFDataRef
//import platform.CoreFoundation.CFStringRef
//import platform.CoreFoundation.CFTypeRef
//import platform.Foundation.CFBridgingRelease
//import platform.Foundation.CFBridgingRetain
//import platform.Foundation.NSData
//import platform.Foundation.NSDate
//import platform.Foundation.NSDateFormatter
//import platform.ImageIO.CGImageSourceCopyPropertiesAtIndex
//import platform.ImageIO.CGImageSourceCreateWithData
//import platform.ImageIO.kCGImagePropertyExifDateTimeDigitized
//import platform.ImageIO.kCGImagePropertyExifDateTimeOriginal
//import platform.ImageIO.kCGImagePropertyExifDictionary
//import platform.ImageIO.kCGImagePropertyPixelHeight
//import platform.ImageIO.kCGImagePropertyPixelWidth
//
//class Exif {
//
//    fun getExifData(data: NSData): ExifData? {
//        val dataRef = CFBridgingRetain(data) as? CFDataRef ?: return null
//
//        val imageSource = CGImageSourceCreateWithData(
//            data = dataRef,
//            options = null
//        ) ?: return null
//        val imageProperties = CGImageSourceCopyPropertiesAtIndex(
//            isrc = imageSource,
//            index = 0UL,
//            options = null
//        ) ?: return null
//
//        val imageProps = CFBridgingRelease(imageProperties as CFTypeRef) as? Map<String, Any> ?: return null
//
//        // Extract the relevant information from the image properties
//        val pixelWidth = imageProps[kCGImagePropertyPixelWidth?.key] as? Double
//        val pixelHeight = imageProps[kCGImagePropertyPixelHeight?.key] as? Double
//
//        val exifData = imageProps[kCGImagePropertyExifDictionary?.key] as? Map<String, Any> ?: mapOf()
//
//        val dateTimeDigitized = (exifData[kCGImagePropertyExifDateTimeDigitized?.key] as? String).orEmpty()
//        val dateTimeOriginal = (exifData[kCGImagePropertyExifDateTimeOriginal?.key] as? String).orEmpty()
//
//        // Convert the date strings to NSDate objects
//        val dateFormatter = NSDateFormatter()
//        dateFormatter.dateFormat = "yyyy:MM:dd HH:mm:ss"
//        val dateDigitized = dateFormatter.dateFromString(dateTimeDigitized)
//        val dateOriginal = dateFormatter.dateFromString(dateTimeOriginal)
//
//        // Create and return an ExifData object
//        return ExifData(
//            pixelWidth = pixelWidth,
//            pixelHeight = pixelHeight,
//            dateTimeDigitized = dateDigitized,
//            dateTimeOriginal = dateOriginal
//        )
//    }
//
//    private val CFStringRef.key: String
//        get() = (CFBridgingRelease(this) as? String).orEmpty()
//
//}
//
//private data class ExifData(
//    val pixelWidth: Double? = null,
//    val pixelHeight: Double? = null,
//    val dateTimeDigitized: NSDate? = null,
//    val dateTimeOriginal: NSDate? = null
//)