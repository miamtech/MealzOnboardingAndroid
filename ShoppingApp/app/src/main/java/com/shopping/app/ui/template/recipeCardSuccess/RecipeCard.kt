package com.shopping.app.ui.template.recipeCardSuccess

import ai.mealz.sdk.components.recipeCard.success.RecipeCardSuccess
import ai.mealz.sdk.components.recipeCard.success.RecipeCardSuccessParams
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

open class RecipeCardSuccess : RecipeCardSuccess {
    /**
     * Step 14 : create template for RecipeCard
     */
    @Composable
    override fun Content(params: RecipeCardSuccessParams) {
        Card(Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ).fillMaxWidth()) {
            Row(Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = params.recipePicture,
                    contentDescription = "Recipe Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = params.recipeTitle)
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(onClick = { params.goToDetail() }) {
                        Text(text = if(params.isInCart)  "Voire" else "Ajouter au panier"     )
                    }
                }
            }
        }
    }
}