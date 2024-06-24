package com.shopping.app.utils

import android.content.Context

object MealzManager {

    private var isInitialized = false
    /**
     * TODO (Step 3): Add the supplier key
     * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
     */
    const val  SUPPLIER_KEY =

    fun initialize(applicationContext: Context){
        if(isInitialized) return
        /**
         * TODO (Step 4): Initialize the Mealz SDK
         * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
         */
        isInitialized = true
    }
}