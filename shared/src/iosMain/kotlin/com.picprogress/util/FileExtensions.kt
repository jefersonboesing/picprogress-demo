@file:OptIn(ExperimentalForeignApi::class)

package com.picprogress.util

import com.picprogress.model.photo.PhotoPath
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.temporaryDirectory

fun NSFileManager.getDocumentsDirectory(): NSURL = throwError { errorPointer ->
    URLForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = errorPointer
    ) ?: throw IllegalStateException()
}

fun PhotoPath.getAbsolutePath(): String {
    val fileManager = NSFileManager.defaultManager
    fileManager.temporaryDirectory.path?.let {
        if (path.startsWith(it)) return path
    }
    fileManager.getDocumentsDirectory().path?.let {
        if (path.startsWith(it)) return path
    }
    return fileManager.getDocumentsDirectory().append(path).path ?: throw IllegalStateException()
}

fun NSURL.append(
    component: String,
    isDirectory: Boolean = false,
): NSURL {
    return URLByAppendingPathComponent(component, isDirectory)
        ?: throw RuntimeException("could not create record directory url")
}

fun NSURL.create(): NSURL {
    val fileManager = NSFileManager.defaultManager
    path?.let {
        if (!fileManager.fileExistsAtPath(it)) {
            throwError { errorPointer ->
                fileManager.createDirectoryAtURL(this, true, null, errorPointer)
            }
        }
    }
    return this
}

fun <T> throwError(block: (errorPointer: CPointer<ObjCObjectVar<NSError?>>) -> T): T {
    memScoped {
        val errorPointer: CPointer<ObjCObjectVar<NSError?>> = alloc<ObjCObjectVar<NSError?>>().ptr
        val result: T = block(errorPointer)
        val error: NSError? = errorPointer.pointed.value
        if (error != null) {
            throw NSErrorException(error)
        } else {
            return result
        }
    }
}

class NSErrorException(val nsError: NSError) : Exception(nsError.toString())