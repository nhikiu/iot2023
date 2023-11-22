package com.example.fire.data.dto.prank

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by TruyenDev on 29/11/2022.
 */

@Parcelize
data class SoundFolderPrank(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "createdAt")
    val createdAt: Long = 0L,
    @Json(name = "name")
    val name: String = "",
    @Json(name = "image")
    val image: String = "",
    @Json(name = "reward")
    val reward: Int ?=null,
    val arraySound: MutableList<SoundPrank> = mutableListOf<SoundPrank>()

) : Parcelable