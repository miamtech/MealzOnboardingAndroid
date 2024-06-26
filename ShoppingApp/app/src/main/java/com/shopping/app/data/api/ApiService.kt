package com.shopping.app.data.api

import com.shopping.app.data.model.MealzProductWrapper
import com.shopping.app.data.model.MealzStoreWrapper
import com.shopping.app.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("items/search?ingredient_id=1")
    fun getProducts(
        @Query("point_of_sale_id") pointOfSaleId: Int
    ): Call<MealzProductWrapper>

    @GET("point-of-sales?filter[supplier-id]=${Constants.SUPPLIER_ID}")
    fun getStore(): Call<MealzStoreWrapper>


    @GET("items/search")
    fun getProductsByCategory(
        @Query("name") category: String,
        @Query("point_of_sale_id") pointOfSaleId: Int,
    ): Call<MealzProductWrapper>


    @GET("items?point_of_sale_id=464")
    fun getProductsById(
        @Query("filter[ext_id]") productId: String,
    ): Call<MealzProductWrapper>
}