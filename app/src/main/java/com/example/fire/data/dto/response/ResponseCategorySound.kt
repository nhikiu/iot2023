package com.example.fire.data.dto.response

import android.os.Parcelable
import com.example.fire.data.dto.prank.SoundFolderPrank
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by TruyenDev on 29/11/2022.
 */
@Parcelize
data class ResponseCategorySound(
    @Json(name = "status")
    val status: String = "",
    @Json(name = "code")
    val number: Int = 0,
    @Json(name = "data")
    val data: List<SoundFolderPrank> = listOf(),
    @Json(name = "myPage")
    val myPage: MyPage? = null
) : Parcelable