package com.example.fire.data.dto.response

import android.os.Parcelable
import com.example.fire.data.dto.prank.PrankRecordFolder
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by TruyenDev on 29/11/2022.
 */
@Parcelize
data class ResponsePrankRecordFolder(
    @Json(name = "status")
    val status: String = "",
    @Json(name = "code")
    val code: Int = 0,
    @Json(name = "data")
    val data: List<PrankRecordFolder> = listOf(),
    @Json(name = "myPage")
    val myPage: MyPage? = null
) : Parcelable