package com.shopping.app.data.repository.search

import com.shopping.app.data.model.MealzProductWrapper
import retrofit2.Call

interface SearchRepository {

    fun getProducts(storeId: String): Call<MealzProductWrapper>

    fun getProductsByCategory(storeId: String, category: String): Call<MealzProductWrapper>

}