package com.shopping.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shopping.app.R
import com.shopping.app.utils.MealzManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Step 5: Call the initialize method of the MealzManager object
         * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit
         */
        MealzManager.initialize(this)
    }
}