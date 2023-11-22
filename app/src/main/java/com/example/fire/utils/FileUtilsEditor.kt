package com.example.fire.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class FileUtilsEditor(private val context: Context) {

    fun saveExternal(bitmap: Bitmap, fileName: String, extention: String, type: String, folderName: String, pathSuccessSave: (path: String) -> Unit) {
        saveImageToStorage(bitmap, "$fileName$extention", type, Environment.DIRECTORY_PICTURES, folderName, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
//            val path = g3.module.mirror.util.FileUtils.getPath(context, it)
            pathSuccessSave(it.toString())
        }
    }

    fun isThanQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun getPathFolder(): String {
        return "/storage/emulated/0/Pictures/${com.example.fire.utils.AppConstCamera.FOLDER_SAVE_PHOTO}"
    }

    private val SAVED_IMAGE_FORMAT = ".png";
    private val SAVED_IMAGE_NAME = System.currentTimeMillis().toString();


    @Suppress("DEPRECATION")
    private fun saveImageToStorage(
        bitmap: Bitmap,
        filename: String = "screenshot.png",
        mimeType: String = "image/jpeg",
        directory: String = Environment.DIRECTORY_PICTURES,
        folderName: String = Environment.DIRECTORY_PICTURES,
        mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        pathSuccess: (uri: Uri) -> Unit
    ) {
        val imageOutStream: OutputStream
        if (isThanQ()) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            }

            context.contentResolver.run {
                val uri =
                    context.contentResolver.insert(mediaContentUri, values)
                        ?: return
                imageOutStream = openOutputStream(uri) ?: return
                pathSuccess(uri)
            }
        } else {
            val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
            val image = File(imagePath, filename)
            imageOutStream = FileOutputStream(image)
        }


        if (mimeType == com.example.fire.utils.AppConstCamera.FORMAT_TYPE_JPG) {
            imageOutStream.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        } else if (mimeType == com.example.fire.utils.AppConstCamera.FORMAT_TYPE_PNG) {
            imageOutStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
    }

    fun addPhotoToGallery(photoPath: String) {

        // Gallery refresh.
        MediaScannerConnection.scanFile(context, arrayOf<String>(photoPath), null) { path, uri -> Log.d("Scan complete for: ", path) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var path: String? = null
            try {
                path = photoPath
            } catch (e: IOException) {
                Log.e("TAG", e.message!!)
            }
            MediaScannerConnection.scanFile(
                context, arrayOf(path), null
            ) { path, uri ->
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = uri
                context.sendBroadcast(mediaScanIntent)
            }
        } else {
            val relationDir: String = photoPath.toString()
            val file1 = File(relationDir)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.absoluteFile)))
        }
    }

    fun pathSaveVideoPrankRecord(): String {
        val appDirctory =File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/PrankRecord")
        appDirctory.mkdirs()
        return appDirctory.absolutePath
    }


}