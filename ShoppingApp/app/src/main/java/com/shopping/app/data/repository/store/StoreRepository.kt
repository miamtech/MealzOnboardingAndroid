package com.shopping.app.data.repository.store

import com.shopping.app.data.model.MealzStoreWrapper
import retrofit2.Call

interface StoreRepository {
    fun getProducts(): Call<MealzStoreWrapper>
}