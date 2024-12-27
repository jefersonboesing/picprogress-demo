package com.picprogress.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import java.io.File

fun Uri.toBitmapOrNull(context: Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        } else {
            ImageDecoder.createSource(context.contentResolver, this).run { ImageDecoder.decodeBitmap(this) }
        }
    } catch (ex: Exception) {
        null
    }
}

fun Bitmap.toJpgTempFileOrNull(context: Context, tempFolder: String): File? {
    return try {
        val temporaryFolder = File(context.filesDir, tempFolder).also { file ->
            file.deleteRecursively()
            file.mkdirs()
        }

        File(temporaryFolder, "file-${System.currentTimeMillis()}.jpg").also {
            it.outputStream().use { fos ->
                compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
        }
    } catch (ex: Exception) {
        null
    }
}

fun Uri.getLastModified(context: Context): Long {
    try {
        DocumentFile.fromSingleUri(context, this)?.lastModified()?.takeIf { it > 0 }?.let { return it }
        getLongQueryColumn(context, MediaStore.Images.Media.DATE_MODIFIED).takeIf { it > 0 }?.let { return it }
        getLongQueryColumn(context, MediaStore.Images.Media.DATE_TAKEN).takeIf { it > 0 }?.let { return it }
        return System.currentTimeMillis()
    } catch (ex: Exception) {
        return System.currentTimeMillis()
    }
}

private fun Uri.getLongQueryColumn(context: Context, column: String): Long {
    context.contentResolver.query(this, arrayOf(column), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            return cursor.getLong(cursor.getColumnIndexOrThrow(column))
        }
    }
    return 0
}
