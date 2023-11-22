package com.example.fire.data.dto.prank

import android.net.Uri
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by TruyenDev on 28/11/2022.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class MergeVideo(
    @Json(name = "urlFeelings")
    val urlFeelings: Uri,
    @Json(name = "urlClip")
    val urlClip: String = "",
    @Json(name = "type")
    val type: String = "",
) : Parcelable