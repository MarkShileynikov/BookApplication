package com.example.mybookapplication.data.utils

import android.content.Context
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
    private fun getFile(context: Context, uri: Uri): File {
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
}
