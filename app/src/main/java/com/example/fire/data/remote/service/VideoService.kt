package com.example.fire.data.remote.service

import com.example.fire.data.dto.response.ResponseVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by TruyenDev
 */

interface VideoService {
    @GET("videotroll/search")
    suspend fun fetchVideo(@Query("filter") filter: String, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Response<ResponseVideo>
}
