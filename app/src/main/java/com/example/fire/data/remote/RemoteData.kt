package com.example.fire.data.remote

import com.example.fire.data.Resource
import com.example.fire.data.dto.frames.DataFrames
import com.example.fire.data.dto.recipes.Recipes
import com.example.fire.data.dto.recipes.RecipesItem
import com.example.fire.data.dto.response.*
import com.example.fire.data.error.NETWORK_ERROR
import com.example.fire.data.error.NO_INTERNET_CONNECTION
import com.example.fire.data.remote.service.*
//import com.example.worldcup2022.data.remote.service.FramesService
import com.example.fire.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


/**
 * Created by TruyenIT
 */

class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) : RemoteDataSource {
    override suspend fun requestRecipes(): Resource<Recipes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchRecipes)) {
            is List<*> -> {
                Resource.Success(data = Recipes(response as ArrayList<RecipesItem>))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestFrames(): Resource<DataFrames> {
        val framesService = serviceGenerator.createService(FramesService::class.java)
        return when (val response = processCall(framesService::fetchFrames)) {
            is DataFrames -> {
                Resource.Success(data = response as DataFrames)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestSoundCategory(filter: String): Resource<ResponseCategorySound> {
        val categorySoundService = serviceGenerator.createService(SoundCategoryService::class.java)
        return when (val response = processCall { categorySoundService.fetchCategorySoynd(filter, 0, 100) }) {
            is ResponseCategorySound -> {
                Resource.Success(data = response as ResponseCategorySound)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestSound(filter: String): Resource<ResponseSound> {
        val soundService = serviceGenerator.createService(SoundService::class.java)
        return when (val response = processCall { soundService.fetchSound(filter, 0, 100, "name") }) {
            is ResponseSound -> {
                Resource.Success(data = response as ResponseSound)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestVideo(filter: String): Resource<ResponseVideo> {
        val videoService = serviceGenerator.createService(VideoService::class.java)
        return when (val response = processCall { videoService.fetchVideo(filter, 0, 100) }) {
            is ResponseVideo -> {
                Resource.Success(data = response as ResponseVideo)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCall(filter: String): Resource<ResponsePrankCall> {
        val callContactService = serviceGenerator.createService(CallContactService::class.java)
        return when (val response = processCall { callContactService.fetchCallContact(filter, 0, 100) }) {
            is ResponsePrankCall -> {
                Resource.Success(data = response as ResponsePrankCall)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCategoryGif(filter: String): Resource<ResponsePrankRecordFolder> {
        val categoryGifService = serviceGenerator.createService(PrankFolderGifService::class.java)
        return when (val response = processCall { categoryGifService.fetchCategoryGif(filter, 0, 100) }) {
            is ResponsePrankRecordFolder -> {
                Resource.Success(data = response as ResponsePrankRecordFolder)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestCategoryVideo(filter: String): Resource<ResponsePrankRecordFolder> {
        val categoryVideoService = serviceGenerator.createService(PrankFolderVideoService::class.java)
        return when (val response = processCall { categoryVideoService.fetchCategoryVideo(filter, 0, 100) }) {
            is ResponsePrankRecordFolder -> {
                Resource.Success(data = response as ResponsePrankRecordFolder)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestItemGif(filter: String): Resource<ResponsePrankRecordItem> {
        val itemGifService = serviceGenerator.createService(PrankItemGifService::class.java)
        return when (val response = processCall { itemGifService.fetchItemGif(filter, 0, 1000, "name") }) {
            is ResponsePrankRecordItem -> {
                Resource.Success(data = response as ResponsePrankRecordItem)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun requestItemVideo(filter: String): Resource<ResponsePrankRecordItem> {
        val itemVideoService = serviceGenerator.createService(PrankItemVideoService::class.java)
        return when (val response = processCall { itemVideoService.fetchItemVideo(filter, 0, 1000, "name") }) {
            is ResponsePrankRecordItem -> {
                Resource.Success(data = response as ResponsePrankRecordItem)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}
