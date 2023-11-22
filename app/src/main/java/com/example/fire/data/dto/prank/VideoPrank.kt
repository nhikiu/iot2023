package com.example.fire.data.dto.prank


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by TruyenDev on 22/11/2022.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class VideoPrank(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "createdAt")
    val createdAt: Long = 0L,
    @Json(name = "importId")
    val importId: String = "",
    @Json(name = "title")
    val name: String = "",
    @Json(name = "file")
    val file: String? = null,
    @Json(name = "credit")
    val credit: String = "",
    @Json(name = "sound")
    val sound: SoundPrank?=null,
    @Json(name = "videoFinal")
    val videoFinal: String = "",
    @Json(name = "thumbnail")
    val thumbnail: String = ""
) : Parcelable