package com.example.fire.data.dto.prank

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by TruyenDev on 22/11/2022.
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class SoundPrank(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "importId")
    val importId: String? = null,
    @Json(name = "groupId")
    val groupId: String = "",
    @Json(name = "group")
    val group: SoundFolderPrank = SoundFolderPrank(),
    @Json(name = "name")
    val name: String = "",
    @Json(name = "linkThumbnail")
    val image: String? = null,
    @Json(name = "linkFile")
    val urlMp3: String = "",
    @Json(name = "createdAt")
    val createdAt: Long = 0L,
) : Parcelable