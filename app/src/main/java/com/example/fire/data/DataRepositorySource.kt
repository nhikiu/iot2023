package com.example.fire.data

import com.example.fire.data.dto.frames.DataFrames
import com.example.fire.data.dto.localprank.MyFolderAudio
import com.example.fire.data.dto.localprank.MyFolderImage
import com.example.fire.data.dto.localprank.MyFolderVideo
import com.example.fire.data.dto.localprank.MyVideo
import com.example.fire.data.dto.login.LoginRequest
import com.example.fire.data.dto.login.LoginResponse
import com.example.fire.data.dto.recipes.Recipes
import com.example.fire.data.dto.response.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by TruyenIT
 */

interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>
    suspend fun addToFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun requestFrames(): Flow<Resource<DataFrames>>
    suspend fun requestCategorySound(filter: String): Flow<Resource<ResponseCategorySound>>
    suspend fun requestSound(filter: String): Flow<Resource<ResponseSound>>
    suspend fun requestVideo(filter: String): Flow<Resource<ResponseVideo>>
    suspend fun requestCall(filter: String): Flow<Resource<ResponsePrankCall>>
    suspend fun getAllImage(): Flow<Resource<List<MyFolderImage>>>
    suspend fun getAllVideoFromFolder(path: String): Flow<Resource<List<MyVideo>>>
    suspend fun getAllAudio(): Flow<Resource<List<MyFolderAudio>>>
    suspend fun getAllVideo(): Flow<Resource<List<MyFolderVideo>>>
    suspend fun requestCategoryGif(filter: String): Flow<Resource<ResponsePrankRecordFolder>>
    suspend fun requestCategoryVideo(filter: String): Flow<Resource<ResponsePrankRecordFolder>>
    suspend fun requestItemGif(filter: String): Flow<Resource<ResponsePrankRecordItem>>
    suspend fun requestItemVideo(filter: String): Flow<Resource<ResponsePrankRecordItem>>
}
