package com.example.fire.data.remote.service

import com.example.fire.data.dto.response.ResponsePrankRecordFolder
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by TruyenDev
 */

interface PrankFolderVideoService {
    @GET("prankrecordvideocate/search")
    suspend fun fetchCategoryVideo(@Query("filter") filter: String, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Response<ResponsePrankRecordFolder>
}
