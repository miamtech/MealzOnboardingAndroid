package com.shopping.app.utils

import ai.mealz.core.Mealz
import ai.mealz.core.init.sdkRequirement
import android.content.Context

object MealzManager {

    private var isInitialized = false
    /**
     * Step 3: Add your supplier key
     * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
     */
    const val supplierKey = "ewoJInN1cHBsaWVyX2lkIjogIjE0IiwKCSJwbGF1c2libGVfZG9tYWluZSI6ICJtaWFtLnRlc3QiLAoJIm1pYW1fb3JpZ2luIjogIm1pYW0iLAoJIm9yaWdpbiI6ICJtaWFtIiwKCSJtaWFtX2Vudmlyb25tZW50IjogIlVBVCIKfQ"


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
             * TODO (Step 10): Add subscriptions configuration
             * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#putting-it-all-together
             */
        }
        isInitialized = true
    }
}