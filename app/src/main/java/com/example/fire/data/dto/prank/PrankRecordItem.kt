package com.example.fire.data.dto.prank

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by TruyenDev on 24/11/2022.
 */
@Parcelize
data class PrankRecordItem(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "createdAt")
    val createdAt: Long? = null,
    @Json(name = "importId")
    val importId: String? = null,
    @Json(name = "type")
    val type: String? = null,
    @Json(name = "thumbnail")
    val thumbnail: String? = null,
    @Json(name = "link")
    val link: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "cate")
    val cate: PrankRecordFolder? = null,
) : Parcelable