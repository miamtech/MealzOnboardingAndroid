package com.shopping.app.data.repository.store

import com.shopping.app.data.api.ApiService
import com.shopping.app.data.model.MealzStoreWrapper
import retrofit2.Call

class StoreRepositoryImpl constructor(private val apiService: ApiService): StoreRepository {
    override fun getProducts(): Call<MealzStoreWrapper> {
       return apiService.getStore()
    }
}