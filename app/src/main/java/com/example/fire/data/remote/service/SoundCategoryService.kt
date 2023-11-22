package com.example.fire.data.remote.service

import com.example.fire.data.dto.response.ResponseCategorySound
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by TruyenDev
 */

interface SoundCategoryService {
    @GET("categorysound/search")
    suspend fun fetchCategorySoynd(@Query("filter") filter: String, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Response<ResponseCategorySound>
}
