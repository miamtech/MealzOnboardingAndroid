package com.shopping.app.utils

import ai.mealz.core.Mealz
import ai.mealz.core.Mealz.basket
import ai.mealz.core.handler.LogHandler
import ai.mealz.core.init.basket
import ai.mealz.core.init.sdkRequirement
import ai.mealz.core.init.subscriptions
import android.content.Context
import com.shopping.app.data.api.ApiClient
import com.shopping.app.data.api.ApiService
import com.shopping.app.data.repository.basket.BasketRepository
import com.shopping.app.data.repository.basket.BasketRepositoryImpl
import com.shopping.app.data.repository.product.ProductRepositoryImpl
import com.shopping.app.ui.basket.viewmodel.BasketViewModel

object MealzManager {

    private var isInitialized = false
    /**
     * Step 3: Add your supplier key
     * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
     */
    const val supplierKey = "ewoJInN1cHBsaWVyX2lkIjogIjE0IiwKCSJwbGF1c2libGVfZG9tYWluZSI6ICJtaWFtLnRlc3QiLAoJIm1pYW1fb3JpZ2luIjogIm1pYW0iLAoJIm9yaWdpbiI6ICJtaWFtIiwKCSJtaWFtX2Vudmlyb25tZW50IjogIlVBVCIKfQ"
    val basketService = BasketViewModel(
        BasketRepositoryImpl(),
        ProductRepositoryImpl(
            ApiClient.getApiService()
        )
    )


    fun initialize(applicationContext: Context){
        if(isInitialized) return
        /**
         * Step 4: Initialize the Mealz SDK
         * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
         */
        Mealz.Core {
            sdkRequirement {
                key = supplierKey
                context = applicationContext
            }
            /**
             * Step 11: Add subscriptions configuration
             * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#putting-it-all-together
             */
            subscriptions {
                basket {
                    // Listen to Miam's basket updates
                    subscribe(basketService)
                    // Push client basket notifications
                    register(basketService)
                }
            }
                // Add your subscription configuration here
        }
        LogHandler.logLevel = LogHandler.LogLevels.ALL_LOGS
        isInitialized = true
    }
}