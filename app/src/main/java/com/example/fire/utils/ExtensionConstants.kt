package com.example.fire.utils

import java.io.File

object ExtensionConstants {
    val PDF = arrayOf(
        "pdf"
    )

    val EXCEL = arrayOf(
        "xls",
        "xlsx"
    )

    val PPT = arrayOf(
        "ppt",
        "pptx"
    )

    val TXT = arrayOf(
        "txt"
    )

    val DOC = arrayOf(
        "doc",
        "docx"
    )

    val WPS = arrayOf(
        "wps"
    )

    val ZIP = arrayOf(
        "zip",
        "rar",
        "iso",
        "7z"
    )

    val VIDEO = arrayOf(
        "mp4"
    )

    val APK = arrayOf(
        "apk"
    )

    val IMAGE = arrayOf(
        "jpeg",
        "jpg",
        "png",
        "psd",
        "eps",
        "gif"
    )

    val MUSIC = arrayOf(
        "mp3",
        "m4a"
    )

    val ALL = arrayListOf(
        *PDF,
        *EXCEL,
        *PPT,
        *TXT,
        *DOC,
        *WPS,
        *ZIP,
        *VIDEO,
        *APK,
        *IMAGE,
        *MUSIC
    )

    fun getTypeFile(path: String): FileType {
        val extension = File(path).extension
        return when {
            PDF.contains(extension) -> FileType.PDF
            EXCEL.contains(extension) -> FileType.EXCEL
            PPT.contains(extension) -> FileType.PPT
            TXT.contains(extension) -> FileType.TXT
            DOC.contains(extension) -> FileType.DOC
            WPS.contains(extension) -> FileType.WPS
            ZIP.contains(extension) -> FileType.ZIP
            VIDEO.contains(extension) -> FileType.VIDEO
            APK.contains(extension) -> FileType.APK
            IMAGE.contains(extension) -> FileType.IMAGE
            MUSIC.contains(extension) -> FileType.MUSIC
            else -> FileType.OTHER
        }
    }


}