package com.shopping.app.data.repository.product

import android.content.Context
import com.shopping.app.data.api.ApiService
import com.shopping.app.data.model.MealzProductWrapper
import com.shopping.app.data.model.Product
import com.shopping.app.data.preference.UserPref
import retrofit2.Call

class ProductRepositoryImpl constructor(private val apiService: ApiService) : ProductRepository {

    override fun getProducts(storeId: String): Call<MealzProductWrapper> {
        return apiService.getProducts(storeId.toInt())
    }

    override fun getProductsById( productId: String ): Call<MealzProductWrapper> {
        return apiService.getProductsById(productId)
    }

}