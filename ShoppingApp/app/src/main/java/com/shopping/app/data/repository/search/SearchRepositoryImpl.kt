package com.shopping.app.data.repository.search

import com.shopping.app.data.api.ApiService
import com.shopping.app.data.model.MealzProductWrapper
import retrofit2.Call

class SearchRepositoryImpl constructor(private val apiService: ApiService) : SearchRepository {

    override fun getProducts(storeId :String): Call<MealzProductWrapper> {
        return apiService.getProducts(storeId.toInt())
    }

    override fun getProductsByCategory(category: String, storeId: String): Call<MealzProductWrapper> {
        return apiService.getProductsByCategory(category, storeId.toInt())
    }

}