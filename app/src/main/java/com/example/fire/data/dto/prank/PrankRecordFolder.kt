package com.example.fire.data.dto.prank

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by TruyenDev on 24/11/2022.
 */
@Parcelize
data class PrankRecordFolder(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "createdAt")
    val createdAt: Long? = null,
    val listVideo: MutableList<PrankRecordItem> = mutableListOf(),
    val listGif: MutableList<PrankRecordItem> = mutableListOf(),
) : Parcelable