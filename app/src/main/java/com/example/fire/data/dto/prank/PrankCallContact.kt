package com.example.fire.data.dto.prank

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by TruyenDev on 29/11/2022.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class PrankCallContact(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "createdAt")
    val createdAt: Long = 0L,
    @Json(name = "importId")
    val importId: String = "",
    @Json(name = "title")
    val title: String = "",
    @Json(name = "linkVideo")
    val linkVideo: String = "",
    @Json(name = "linkAudio")
    val linkAudio: String = "",
    @Json(name = "avatar")
    val avatar: String = "",
    @Json(name = "thumbnail")
    val thumbnail: String = "",
    @Json(name = "phone")
    val phone: String = "",
    ) : Parcelable