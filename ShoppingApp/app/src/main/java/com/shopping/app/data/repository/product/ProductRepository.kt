package com.shopping.app.data.repository.product

import com.shopping.app.data.model.MealzProductWrapper
import retrofit2.Call

interface ProductRepository {

    fun getProducts(storeId: String): Call<MealzProductWrapper>
    fun getProductsById( productId: String): Call<MealzProductWrapper>

}