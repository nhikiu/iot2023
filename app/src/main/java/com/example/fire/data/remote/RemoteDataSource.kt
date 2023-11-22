package com.example.fire.data.remote

import com.example.fire.data.Resource
import com.example.fire.data.dto.frames.DataFrames
import com.example.fire.data.dto.recipes.Recipes
import com.example.fire.data.dto.response.*

/**
 * Created by TruyenIT
 */

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Recipes>
    suspend fun requestFrames(): Resource<DataFrames>
    suspend fun requestSoundCategory(filter: String): Resource<ResponseCategorySound>
    suspend fun requestSound(filter: String): Resource<ResponseSound>
    suspend fun requestVideo(filter: String): Resource<ResponseVideo>
    suspend fun requestCall(filter: String): Resource<ResponsePrankCall>
    suspend fun requestCategoryGif(filter: String): Resource<ResponsePrankRecordFolder>
    suspend fun requestCategoryVideo(filter: String): Resource<ResponsePrankRecordFolder>
    suspend fun requestItemGif(filter: String): Resource<ResponsePrankRecordItem>
    suspend fun requestItemVideo(filter: String): Resource<ResponsePrankRecordItem>
}
