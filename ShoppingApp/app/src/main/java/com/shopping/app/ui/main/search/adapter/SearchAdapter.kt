package com.shopping.app.ui.main.search.adapter

import ai.mealz.core.model.SuggestionsCriteria
import ai.mealz.sdk.components.recipeJourney.RecipeJourney
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.shopping.app.R
import com.shopping.app.data.model.Product
import com.shopping.app.databinding.ItemProductSearchBinding
import com.shopping.app.databinding.ItemRecipeBinding
import com.shopping.app.utils.Constants

class SearchAdapter(
      private val navController: NavController,
      private val productList: List<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * STEP 13 : Add Mealz to you view
     */

    // You can handle eather the position or the section were Mealz will apear
    val recipePosition = listOf(2,7,10)

    override fun getItemViewType(position: Int): Int {
        return if (recipePosition.contains(position)) {
            R.layout.item_recipe
        } else {
           R.layout.item_product_search
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return  when(viewType){
            R.layout.item_product_search -> {
                return  SearchProductViewHolder(ItemProductSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                return SearchRecipeViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            R.layout.item_product_search -> {
                (holder as SearchProductViewHolder).bind(productList[position])
            }
            R.layout.item_recipe -> {
               val surroundingProductIds = if(getItemCount() >= position +1) listOf(productList[position].id.toString(),productList[position+1].id.toString()) else
                   listOf(productList[position].id.toString())
                (holder as SearchRecipeViewHolder).bind(
                    SuggestionsCriteria(shelfIngredientsIds = surroundingProductIds))
            }
        }
    }

    fun goProductDetails(product: Product?){

        navController.navigate(
            R.id.action_searchFragment_to_productDetailsFragment,
            Bundle().apply {
                putString(Constants.PRODUCT_MODEL_NAME, product?.toJson())
            })

    }

    inner class SearchProductViewHolder(private val binding: ItemProductSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.dataHolder = product
            binding.searchAdapter = this@SearchAdapter
            binding.executePendingBindings()
        }
    }

    inner class SearchRecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(criteria: SuggestionsCriteria ) {
            (this.itemView as RecipeJourney).bind(criteria = criteria)
            binding.dataHolder = criteria
            binding.searchAdapter = this@SearchAdapter
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = productList.size

}