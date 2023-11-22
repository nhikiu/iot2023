package com.example.fire.data.remote.service

import com.example.fire.data.dto.recipes.RecipesItem
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by TruyenIT
 */

interface RecipesService {
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<RecipesItem>>
}
