package com.shopping.app.utils

import ai.mealz.sdk.components.MiamTheme
import ai.mealz.sdk.components.MiamTheme.recipeCard
import com.shopping.app.ui.template.recipeCardSuccess.RecipeCardSuccess

class MealzTemplateManger {
    /**
     * Step 15 : Template configuration
     */
    init {
        MiamTheme.Template {
            recipeCard {
                success {
                    view = RecipeCardSuccess()
                }
            }
        }
    }
}