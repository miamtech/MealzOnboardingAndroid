package com.shopping.app.ui.main.product.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shopping.app.data.repository.product.ProductRepository

class ProductViewModelFactory(private val  context: Context,private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(context, productRepository) as T
    }
}