package com.example.mybookapplication.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

object ImageUtils {
    @Throws(IOException::class)
    fun getRequestBodyForFolderImages(context: Context, uri: Uri): RequestBody {
        val file = getFile(context, uri)
        return file.asRequestBody("image/jpg".toMediaTypeOrNull())
    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File {
        var destinationFilename: File
        try {
            destinationFilename = File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        } catch (e: AssertionError) {
            destinationFilename = File(uri.path!!)
        }

        context.contentResolver.openInputStream(uri)?.use { ins ->
            createFileFromStream(ins, destinationFilename)
        } ?: Log.e("Save File", "Input stream is null")
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message ?: "Exception occurred")
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        context.contentResolver.query(uri, null, null, null, null)?.use { returnCursor ->
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            return returnCursor.getString(nameIndex)
        }
        return ""
    }


    fun compressImage(context: Context, fileUri: Uri): File? {
        val file = getFile(context, fileUri)
        val filePath = file.path ?: return null
        val rotatedBitmap = rotateBitmapIfNeeded(filePath)

        val fileSizeInBytes = File(filePath).length()
        val fileSizeInKB = fileSizeInBytes / 1024

        val compressionQuality = when {
            fileSizeInKB > 5120 -> 10
            fileSizeInKB > 2048 -> 15
            fileSizeInKB > 1024 -> 30
            else -> 40
        }

        val tempFile = createTempImageFile(context) ?: return null
        val outputStream = FileOutputStream(tempFile)
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream)
        outputStream.flush()
        outputStream.close()

        return tempFile
    }

    private fun rotateBitmapIfNeeded(filePath: String): Bitmap {
        val bitmap = BitmapFactory.decodeFile(filePath)

        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun createTempImageFile(context: Context): File? {
        val tempFileName = "avatar"
        val storageDir = context.cacheDir
        return File.createTempFile(tempFileName, ".jpg", storageDir)
    }
}
